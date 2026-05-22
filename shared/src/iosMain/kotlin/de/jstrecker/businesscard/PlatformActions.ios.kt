package de.jstrecker.businesscard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.uikit.LocalUIViewController
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.*
import platform.PhotosUI.*
import platform.UIKit.UIApplication
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.darwin.NSObject
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

class IOSPlatformActions : PlatformActions {
    override fun openUrl(url: String) {
        val nsUrl = NSURL.URLWithString(url)
        if (nsUrl != null) {
            UIApplication.sharedApplication.openURL(nsUrl)
        }
    }

    override fun makeCall(phone: String) {
        val nsUrl = NSURL.URLWithString("tel:$phone")
        if (nsUrl != null) {
            UIApplication.sharedApplication.openURL(nsUrl)
        }
    }

    override fun sendEmail(email: String) {
        val nsUrl = NSURL.URLWithString("mailto:$email")
        if (nsUrl != null) {
            UIApplication.sharedApplication.openURL(nsUrl)
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    @Composable
    override fun rememberImagePicker(onImagePicked: (String) -> Unit): () -> Unit {
        val viewController = LocalUIViewController.current

        val delegate = remember {
            object : NSObject(), PHPickerViewControllerDelegateProtocol {
                override fun picker(picker: PHPickerViewController, didFinishPicking: List<*>) {
                    picker.dismissViewControllerAnimated(true, null)

                    val result = didFinishPicking.firstOrNull() as? PHPickerResult ?: return
                    val itemProvider = result.itemProvider

                    // 1. Use type identifiers instead of Objective-C class references
                    if (itemProvider.hasItemConformingToTypeIdentifier("public.image")) {
                        itemProvider.loadDataRepresentationForTypeIdentifier("public.image") { data, _ ->
                            if (data != null) {
                                // Convert to UIImage to apply your JPEG compression
                                val image = UIImage(data = data)
                                val jpegData = UIImageJPEGRepresentation(image, 0.8)
                                if (jpegData != null) {
                                    val fileManager = NSFileManager.defaultManager
                                    val urls = fileManager.URLsForDirectory(NSDocumentDirectory, NSUserDomainMask)
                                    val documentsDirectory = urls.first() as? NSURL
                                    val fileUrl =
                                        documentsDirectory?.URLByAppendingPathComponent("profile_image.jpg")

                                    if (fileUrl != null) {
                                        jpegData.writeToURL(fileUrl, true)

                                        dispatch_async(dispatch_get_main_queue()) {
                                            onImagePicked(fileUrl.path ?: "")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return {
            val configuration = PHPickerConfiguration()
            configuration.filter = PHPickerFilter.imagesFilter
            configuration.selectionLimit = 1

            val picker = PHPickerViewController(configuration)
            picker.delegate = delegate
            viewController.presentViewController(picker, true, null)
        }
    }
}

actual fun getPlatformActions(): PlatformActions = IOSPlatformActions()
