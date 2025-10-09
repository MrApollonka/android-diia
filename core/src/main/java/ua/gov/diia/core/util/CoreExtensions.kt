package ua.gov.diia.core.util

import ua.gov.diia.core.BuildConfig
import ua.gov.diia.core.util.CommonConst.BUILD_TYPE_AUTOMATION
import ua.gov.diia.core.util.CommonConst.BUILD_TYPE_DEBUG
import ua.gov.diia.core.util.CommonConst.BUILD_TYPE_RELEASE
import ua.gov.diia.core.util.CommonConst.BUILD_TYPE_STAGE

fun isReleaseMode(): Boolean = BuildConfig.BUILD_TYPE == BUILD_TYPE_RELEASE

fun isDebugMode(): Boolean = BuildConfig.BUILD_TYPE == BUILD_TYPE_DEBUG

fun isStageMode(): Boolean = BuildConfig.BUILD_TYPE == BUILD_TYPE_STAGE

fun isAutomationMode(): Boolean = BuildConfig.BUILD_TYPE == BUILD_TYPE_AUTOMATION

fun isDevMode(): Boolean = !isReleaseMode()