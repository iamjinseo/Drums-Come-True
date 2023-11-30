/*
 * Copyright 2023 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ssafy.drumscometrue.freePlay

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark
import com.ssafy.drumscometrue.R
import com.ssafy.drumscometrue.freePlay.fragment.CameraFragment
import kotlin.math.max
import kotlin.math.min

/**
 * Android앱에서 사용되는 OverlayView클래스 정의 -> 다른 뷰 위에 그려지는 사용자 지정 뷰
 * Pose Landmarker결과를 화면에 표시(그리는데)하는 데 사용
 * */

class OverlayView(context: Context?, attrs: AttributeSet?) :
    View(context, attrs) {

    private var results: Pose? = null
    private var pointPaint = Paint()
    private var linePaint = Paint()
    private var stickPaint = Paint()
    private var imageWidth: Int = 1
    private var imageHeight: Int = 1
    private var leftPoint: CameraFragment.Point? = null
    private var rightPoint: CameraFragment.Point? = null

    init {
        initPaints()
    }

    fun setResults(pose: Pose, imageHeight: Int, imageWidth: Int, leftPoint: CameraFragment.Point, rightPoint:CameraFragment.Point) {
        results = pose
        this.imageHeight = imageHeight
        this.imageWidth = imageWidth
        this.leftPoint = leftPoint
        this.rightPoint = rightPoint

        invalidate() // 뷰를 다시 그리도록 요청
    }

    fun setResultsBass(pose: Pose, imageHeight: Int, imageWidth: Int, leftPoint: CameraFragment.Point, rightPoint:CameraFragment.Point) {
        results = pose
        this.imageHeight = imageHeight
        this.imageWidth = imageWidth
        this.leftPoint = leftPoint
        this.rightPoint = rightPoint

        invalidate() // 뷰를 다시 그리도록 요청
    }

    private fun initPaints() {
        linePaint.color = ContextCompat.getColor(context!!, R.color.mp_color_secondary)
        linePaint.strokeWidth = LANDMARK_STROKE_WIDTH
        linePaint.style = Paint.Style.STROKE

        stickPaint.color = ContextCompat.getColor(context!!, R.color.green)
        stickPaint.strokeWidth = LANDMARK_STROKE_WIDTH
        stickPaint.style = Paint.Style.STROKE

        pointPaint.color = Color.YELLOW
        pointPaint.strokeWidth = LANDMARK_STROKE_WIDTH
        pointPaint.style = Paint.Style.FILL
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        results?.let { pose ->
            canvas.save() // 현재 캔버스 상태 저장

            // 뷰를 가로 방향으로 뒤집기
            canvas.scale(-1f, 1f, width / 2f, height / 2f)

            if(leftPoint != null){
                canvas.drawPoint(
                    leftPoint!!.x / imageHeight * width , // 좌표를 화면비에 맞춰 조정
                    leftPoint!!.y /imageWidth * height, // 좌표를 화면비에 맞춰 조정
                    pointPaint
                )
            }
            if(rightPoint != null){
                canvas.drawPoint(
                    rightPoint!!.x / imageHeight * width , // 좌표를 화면비에 맞춰 조정
                    rightPoint!!.y /imageWidth * height, // 좌표를 화면비에 맞춰 조정
                    pointPaint
                )
            }

            drawPointLine(canvas,pose.getPoseLandmark(20),rightPoint,stickPaint)
            drawPointLine(canvas,pose.getPoseLandmark(19),leftPoint,stickPaint)


//            for (landmark in pose.allPoseLandmarks) {
////                canvas.drawPoint(
////                    landmark.position.x /imageHeight * width , // 좌표를 scaleFactor로 조정
////                    landmark.position.y /imageWidth * height, // 좌표를 scaleFactor로 조정
////                    pointPaint
////                )
//
//                val leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
//                val rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER)
//                val leftElbow = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW)
//                val rightElbow = pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW)
//                val leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST)
//                val rightWrist = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST)
//                val leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP)
//                val rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP)
//                val leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE)
//                val rightKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE)
//                val leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE)
//                val rightAnkle = pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE)
//
//                val leftPinky = pose.getPoseLandmark(PoseLandmark.LEFT_PINKY)
//                val rightPinky = pose.getPoseLandmark(PoseLandmark.RIGHT_PINKY)
//                val leftIndex = pose.getPoseLandmark(PoseLandmark.LEFT_INDEX)
//                val rightIndex = pose.getPoseLandmark(PoseLandmark.RIGHT_INDEX)
//                val leftThumb = pose.getPoseLandmark(PoseLandmark.LEFT_THUMB)
//                val rightThumb = pose.getPoseLandmark(PoseLandmark.RIGHT_THUMB)
//                val leftHeel = pose.getPoseLandmark(PoseLandmark.LEFT_HEEL)
//                val rightHeel = pose.getPoseLandmark(PoseLandmark.RIGHT_HEEL)
//                val leftFootIndex = pose.getPoseLandmark(PoseLandmark.LEFT_FOOT_INDEX)
//                val rightFootIndex = pose.getPoseLandmark(PoseLandmark.RIGHT_FOOT_INDEX)
//
//
////                drawLine(canvas, leftShoulder, rightShoulder, linePaint)
////                drawLine(canvas, leftHip, rightHip, linePaint)
//
//                // Left body
//                drawLine(canvas, leftShoulder, leftElbow, linePaint)
//                drawLine(canvas, leftElbow, leftWrist, linePaint)
//                drawLine(canvas, leftShoulder, leftHip, linePaint)
//                drawLine(canvas, leftHip, leftKnee, linePaint)
//                drawLine(canvas, leftKnee, leftAnkle, linePaint)
//                drawLine(canvas, leftWrist, leftThumb, linePaint)
//                drawLine(canvas, leftWrist, leftPinky, linePaint)
//                drawLine(canvas, leftWrist, leftIndex, linePaint)
//                drawLine(canvas, leftIndex, leftPinky, linePaint)
//                drawLine(canvas, leftAnkle, leftHeel, linePaint)
//                drawLine(canvas, leftHeel, leftFootIndex, linePaint)
//
//                // Right body
//                drawLine(canvas, rightShoulder, rightElbow, linePaint)
//                drawLine(canvas, rightElbow, rightWrist, linePaint)
//                drawLine(canvas, rightShoulder, rightHip, linePaint)
//                drawLine(canvas, rightHip, rightKnee, linePaint)
//                drawLine(canvas, rightKnee, rightAnkle, linePaint)
//                drawLine(canvas, rightWrist, rightThumb, linePaint)
//                drawLine(canvas, rightWrist, rightPinky, linePaint)
//                drawLine(canvas, rightWrist, rightIndex, linePaint)
//                drawLine(canvas, rightIndex, rightPinky, linePaint)
//                drawLine(canvas, rightAnkle, rightHeel, linePaint)
//                drawLine(canvas, rightHeel, rightFootIndex, linePaint)
//            }
        }
    }

    fun drawLine(
        canvas: Canvas,
        startLandmark: PoseLandmark?,
        endLandmark: PoseLandmark?,
        paint: Paint
    ){
        val start = startLandmark!!.position3D
        val end = endLandmark!!.position3D

        // Gets average z for the current body line
        val avgZInImagePixel = (start.z + end.z) / 2

        canvas.drawLine(
            start.x /imageHeight * width,
            start.y /imageWidth * height,
            end.x /imageHeight * width,
            end.y /imageWidth * height,
            paint
        )
    }

    fun drawPointLine(
        canvas: Canvas,
        startLandmark: PoseLandmark?,
        endPoint: CameraFragment.Point?,
        paint: Paint
    ){
        val start = startLandmark!!.position
        val end = endPoint!!

        canvas.drawLine(
            start.x /imageHeight * width,
            start.y /imageWidth * height,
            end.x /imageHeight * width,
            end.y /imageWidth * height,
            paint
        )
    }

    companion object {
        private const val LANDMARK_STROKE_WIDTH = 20F
    }
}
