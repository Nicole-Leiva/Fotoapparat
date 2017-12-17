package io.fotoapparat.capability.provide

import android.hardware.Camera
import io.fotoapparat.capability.Capabilities
import io.fotoapparat.parameter.SupportedParameters
import io.fotoapparat.parameter.camera.convert.toFlash
import io.fotoapparat.parameter.camera.convert.toFocusMode
import io.fotoapparat.parameter.camera.convert.toFpsRange
import io.fotoapparat.parameter.camera.convert.toResolution

/**
 * Returns the [io.fotoapparat.capability.Capabilities] of the given [Camera].
 */
internal fun Camera.getCapabilities() = SupportedParameters(parameters).getCapabilities()

private fun SupportedParameters.getCapabilities(): Capabilities {
    return Capabilities(
            canZoom = supportedZoom,
            flashModes = flashModes.extract { it.toFlash() },
            focusModes = focusModes.extract { it.toFocusMode() },
            canSmoothZoom = supportedSmoothZoom,
            sensorSensitivities = sensorSensitivities.toSet(),
            previewFpsRanges = supportedPreviewFpsRanges.extract { it.toFpsRange() },
            pictureResolutions = pictureResolutions.mapSizes(),
            previewResolutions = previewResolutions.mapSizes()
    )
}

private fun <Parameter, Code> List<Code>.extract(converter: (Code) -> Parameter) = map { converter.invoke(it) }.toSet()

private fun Collection<Camera.Size>.mapSizes() = map { it.toResolution() }.toSet()