package com.peterdang.androidcatchup.core.constant

sealed class Constants {
    open class FunctionCode {
        companion object {
            const val ROOM = "room"
            const val WORKMANAGER = "workmanager"
            const val PAGGING = "pagging"
            const val SLICE = "slice"
            const val DOWNLOAD_MANAGER = "download_manager"
            const val UI = "ui"
        }
    }

    open class BlurFeature {
        companion object {
            const val KEY_IMAGE_URI = "KEY_IMAGE_URI"
            const val OUTPUT_PATH = "blur_filter_outputs"
            const val IMAGE_MANIPULATION_WORK_NAME = "image_manipulation_work_name"
            const val TAG_OUTPUT = "output_save_img"
        }
    }

    open class RequestCode {
        companion object {
            const val REQUEST_CODE_IMAGE = 100
            const val REQUEST_CODE_PERMISSIONS = 101
        }
    }

    open class NotificationNumber {
        companion object {
            const val WORKMANAGER: Int = 1
        }
    }

}