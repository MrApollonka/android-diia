package ua.gov.diia.scanner.util

import android.graphics.Bitmap
import android.media.Image
import androidx.compose.ui.geometry.Rect
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.common.internal.ImageConvertUtils
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.frame.Frame
import kotlin.math.abs
import kotlin.math.roundToInt

fun prepareInputImage(
    frame: Frame,
    camera: CameraView,
    boundingFrame: Rect
): InputImage? {
    val inputImage = when (frame.dataClass) {
        ByteArray::class.java -> {
            val frameByteArray = frame.getData<ByteArray>()
            InputImage.fromByteArray(
                frameByteArray,
                frame.size.width,
                frame.size.height,
                frame.rotationToUser,
                frame.format
            )
        }

        Image::class.java -> {
            val frameImage = frame.getData<Image>()
            InputImage.fromMediaImage(
                frameImage,
                frame.rotationToUser
            )
        }

        else -> null
    }
    if (inputImage != null) {
        val androidBitmap = inputImage.toAndroidBitmap()
        val imgSize = ImageSize(androidBitmap.width, androidBitmap.height)

        val aspectRatio = imgSize.height.toFloat() / imgSize.width.toFloat()
        val aspectDifference1 = abs(camera.height.toFloat() - aspectRatio * camera.width.toFloat())

        abs(aspectRatio * camera.height.toFloat() - camera.width.toFloat())

        val aspectX = imgSize.width.toFloat() / (camera.width.toFloat() + aspectDifference1)
        val aspectY = imgSize.height.toFloat() / (camera.height.toFloat())

        val left = (boundingFrame.left + aspectDifference1 / 2f) * aspectX
        val top = (boundingFrame.top) * aspectY
        val width = boundingFrame.width * aspectX
        val height = boundingFrame.height * aspectY

        val bb = BoundingBox(left, top, left + width, top + height)

        return androidBitmap.crop(bb)
    }
    return inputImage
}

private fun InputImage.toAndroidBitmap(): Bitmap {
    return this.bitmapInternal ?: ImageConvertUtils.getInstance()
        .getUpRightBitmap(this)
}

private fun Bitmap.crop(boundingBox: BoundingBox): InputImage {
    val croppedBitmap = Bitmap.createBitmap(
        this,
        boundingBox.left.roundToInt(), boundingBox.top.roundToInt(),
        boundingBox.width.roundToInt(),
        boundingBox.height.roundToInt(),
        null,
        true
    )
    return InputImage.fromBitmap(croppedBitmap, 0)
}

private data class BoundingBox(
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float,
) {
    val width = right - left
    val height = bottom - top
}

private data class ImageSize(
    val width: Int,
    val height: Int
)