package com.demo.player;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;
import android.view.Surface;

import com.demo.util.Utils;

public class Renderer implements GLSurfaceView.Renderer,
		SurfaceTexture.OnFrameAvailableListener {
	private static String TAG = "Renderer";

	private int mProgram = 0;
	private int mTextureID = 0;
	private int mvPositionHandle = 0;
	private int mTexCoordsHandle = 0;
	private int muSTMatrixHandle = 0;
	private int muMVPMatrixHandle = 0;

	private Player mPlayer = null;
	private GLSurfaceView mGLSurfaceView = null;
	private SurfaceTexture mSurfaceTexture;
	private boolean updateSurface = false;

	private int videoWidth = 0;
	private int videoHeight = 0;
	private int surfaceWidth = 0;
	private int surfaceHeight = 0;
	private boolean videoAdjustViewport = false;

	public Renderer(Context context, Player player, GLSurfaceView glsurfaceview) {
		mPlayer = player;
		mGLSurfaceView = glsurfaceview;
		mVerticesMatrix = ByteBuffer
				.allocateDirect(mVerticesMatrixData.length * 4)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		mVerticesMatrix.put(mVerticesMatrixData).position(0);

		mTextureMatrix = ByteBuffer
				.allocateDirect(mTextureMatrixData.length * 4)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		mTextureMatrix.put(mTextureMatrixData).position(0);

		Matrix.setIdentityM(mSTMatrix, 0);
	}

	public void setVideoSize(int width, int height) {
		videoWidth = width;
		videoHeight = height;
		videoAdjustViewport = true;
	}

	private void videoAdjustViewport() {
		float heightAspect = videoHeight / (float) surfaceHeight;
		float widthAspect = videoWidth / (float) surfaceWidth;

		Log.i(TAG, "[LTDemo] sHeight=" + surfaceHeight + ",sWidth="
				+ surfaceWidth);
		Log.i(TAG, "[LTDemo] vHeight=" + videoHeight + ",vWidth=" + videoWidth);

		if (heightAspect > widthAspect) {
			float heightRatio = surfaceHeight / (float) videoHeight;
			int newWidth = (int) (videoWidth * heightRatio);
			int xOffset = (surfaceWidth - newWidth) / 2;
			// GLES20.glViewport(xOffset, 0, newWidth, surfaceHeight);
			GLES20.glViewport(0, 0, surfaceWidth, surfaceHeight);
		} else {
			float widthRatio = surfaceWidth / (float) videoWidth;
			int newHeight = (int) (videoHeight * widthRatio);
			int yOffset = (surfaceHeight - newHeight) / 2;
			GLES20.glViewport(0, yOffset, surfaceWidth, newHeight);
		}

		videoAdjustViewport = false;
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		synchronized (this) {
			if (updateSurface) {
				mSurfaceTexture.updateTexImage();
				mSurfaceTexture.getTransformMatrix(mSTMatrix);
				updateSurface = false;
			}
		}

		if (videoAdjustViewport) {
			videoAdjustViewport();
		}

		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

		GLES20.glUseProgram(mProgram);
		checkGlError("glUseProgram");

		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mTextureID);

		GLES20.glVertexAttribPointer(mvPositionHandle, 3, GLES20.GL_FLOAT,
				false, 0, mVerticesMatrix);
		checkGlError("glVertexAttribPointer");
		GLES20.glEnableVertexAttribArray(mvPositionHandle);
		checkGlError("glEnableVertexAttribArray");

		GLES20.glVertexAttribPointer(mTexCoordsHandle, 4, GLES20.GL_FLOAT,
				false, 0, mTextureMatrix);
		checkGlError("glVertexAttribPointer");
		GLES20.glEnableVertexAttribArray(mTexCoordsHandle);
		checkGlError("glEnableVertexAttribArray");

		GLES20.glUniformMatrix4fv(muSTMatrixHandle, 1, false, mSTMatrix, 0);
		Matrix.setIdentityM(mMVPMatrix, 0);
		GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, mMVPMatrix, 0);

		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4);
		checkGlError("glDrawArrays");
		GLES20.glFinish();
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		surfaceWidth = width;
		surfaceHeight = height;
		videoAdjustViewport = true;
		// GLES20.glViewport(0, 0, width, height);
		Log.i(TAG, "[LTDemo] onSurfaceChanged:width=" + width + ",height="
				+ height);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		mProgram = createProgram(mVertexShader, mFragmentShader);
		if (mProgram == 0) {
			Log.e(TAG, "[LTDemo] mProgram = 0 error!");
			return;
		}

		mvPositionHandle = GLES20.glGetAttribLocation(mProgram, "position");
		checkGlError("glGetAttribLocation");
		if (mvPositionHandle == -1) {
			throw new RuntimeException(
					"[LTDemo] Could not get attrib location for position");
		}

		mTexCoordsHandle = GLES20.glGetAttribLocation(mProgram, "texCoords");
		muSTMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uSTMatrix");
		muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

		mTextureID = createTexture(gl, config);
		mRendererCallBack.onSurfaceCreated();

		synchronized (this) {
			updateSurface = false;
		}
	}

	private int createTexture(GL10 gl, EGLConfig config) {
		int[] textures = new int[1];
		GLES20.glGenTextures(1, textures, 0);
		int textureId = textures[0];

		GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId);
		GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
				GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
				GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

		mSurfaceTexture = new SurfaceTexture(textureId);
		mSurfaceTexture.setOnFrameAvailableListener(this);

		Surface surface = new Surface(mSurfaceTexture);
		mPlayer.setSurface(surface);
		surface.release();

		return textureId;
	}

	@Override
	public synchronized void onFrameAvailable(SurfaceTexture arg0) {
		mGLSurfaceView.requestRender();
		updateSurface = true;
	}

	private int loadShader(int shaderType, String source) {
		int shader = GLES20.glCreateShader(shaderType);
		if (shader != 0) {
			GLES20.glShaderSource(shader, source);
			GLES20.glCompileShader(shader);
			int[] compiled = new int[1];
			GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
			if (compiled[0] == 0) {
				Log.e(TAG, "[LTDemo] Could not compile shader " + shaderType
						+ ":");
				Log.e(TAG, GLES20.glGetShaderInfoLog(shader));
				GLES20.glDeleteShader(shader);
				shader = 0;
			}
		}
		return shader;
	}

	private int createProgram(String vertexSource, String fragmentSource) {
		int vertexShader = 0;
		int pixelShader = 0;

		String vertexFile = Utils.getTextFromFile("/data/local/tmp/vertex.txt");
		String fragmentFile = Utils
				.getTextFromFile("/data/local/tmp/fragment.txt");
		if (vertexFile != "" && fragmentFile != "") {
			Log.i(TAG, "[LTDemo] loadShader with text file!");
			vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexFile);
			pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentFile);
		}

		if (vertexShader == 0 || pixelShader == 0) {
			Log.i(TAG, "[LTDemo] loadShader with lib!");
		}

		if (vertexShader == 0 || pixelShader == 0) {
			Log.i(TAG, "[LTDemo] loadShader with var!");
			vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource);
			pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
		}

		if (vertexShader == 0 || pixelShader == 0) {
			Log.i(TAG, "[LTDemo] loadShader error!");
			return 0;
		}

		int program = GLES20.glCreateProgram();
		if (program != 0) {
			GLES20.glAttachShader(program, vertexShader);
			checkGlError("glAttachShader");
			GLES20.glAttachShader(program, pixelShader);
			checkGlError("glAttachShader");
			GLES20.glLinkProgram(program);
			int[] linkStatus = new int[1];
			GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
			if (linkStatus[0] != GLES20.GL_TRUE) {
				Log.e(TAG, "[LTDemo] Could not link program: ");
				Log.e(TAG, GLES20.glGetProgramInfoLog(program));
				GLES20.glDeleteProgram(program);
				program = 0;
			}
		}
		return program;
	}

	private void checkGlError(String op) {
		int error;
		while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
			Log.e(TAG, op + ": glError " + error);
			throw new RuntimeException(op + ": glError " + error);
		}
	}

	private FloatBuffer mVerticesMatrix;
	private final float[] mVerticesMatrixData = { -1.0f, 1.0f, 0.0f, // top left
			-1.0f, -1.0f, 0.0f, // bottom left
			1.0f, -1.0f, 0.0f, // bottom right
			1.0f, 1.0f, 0.0f // top right
	};

	private FloatBuffer mTextureMatrix;
	private final float[] mTextureMatrixData = { 0.0f, 1.0f, 0.0f, 1.0f, 0.0f,
			0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f };

	private float[] mSTMatrix = new float[16];
	private float[] mMVPMatrix = new float[16];

	private final String mVertexShader = "" + "uniform mat4 uSTMatrix;\n"
			+ "uniform mat4 uMVPMatrix;\n" + "attribute vec4 position;\n"
			+ "attribute vec4 texCoords;\n" + "varying vec2 outTexCoords;\n"
			+ "void main(void) {\n"
			+ "    outTexCoords = (uSTMatrix * texCoords).xy;\n"
			+ "    gl_Position = uMVPMatrix * position;\n" + "}\n";

	private final String mFragmentShader = ""
			+ "#extension GL_OES_EGL_image_external : require\n"
			+ "precision mediump float;\n" + "varying vec2 outTexCoords;\n"
			+ "uniform samplerExternalOES texture;\n" + "void main(void) {\n"
			+ "    gl_FragColor = texture2D(texture, outTexCoords);\n" + "}\n";

	// RendererCallBack
	private RendererCallBack mRendererCallBack;

	public interface RendererCallBack {
		public void onSurfaceCreated();
	}

	public void setRendererCallBack(RendererCallBack callback) {
		mRendererCallBack = callback;
	}
}
