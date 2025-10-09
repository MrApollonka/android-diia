package ua.gov.diia.core.util.state

import org.junit.Assert.assertFalse
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LoaderHelperTest {

    @Test
    fun `Loader Component getLegacyProgress isLoading match true`() {
        val loader = Loader.createComponent(key = "key", isLoading = true)
        val progress = loader.getLegacyProgress()
        assertTrue(progress.second)
    }

    @Test
    fun `Loader Component getLegacyProgress isLoading match false`() {
        val loader = Loader.createComponent(key = "key", isLoading = false)
        val progress = loader.getLegacyProgress()
        assertFalse(progress.second)
    }

    @Test
    fun `Loader Component getLegacyProgress key match`() {
        val loader = Loader.createComponent(key = "key", isLoading = false)
        val progress = loader.getLegacyProgress()
        assertEquals(progress.first, "key")
    }


    ///
    @Test
    fun `Loader FullScreen getLegacyContent isLoading match false when isLoading == true`() {
        val loader = Loader.createFullScreen(indicator = TridentDefault, isLoading = true)
        val fullScreen = loader.getLegacyContentLoaded()
        assertFalse(fullScreen.second)
    }

    @Test
    fun `Loader FullScreen getLegacyContent isLoading match true when isLoading == false`() {
        val loader = Loader.createFullScreen(indicator = TridentDefault, isLoading = false)
        val fullScreen = loader.getLegacyContentLoaded()
        assertTrue(fullScreen.second)
    }

    @Test
    fun `Loader FullScreen getLegacyContent key match`() {
        val loader = Loader.createFullScreen(indicator = TridentDefault, isLoading = false)
        val fullScreen = loader.getLegacyContentLoaded()
        assertEquals(fullScreen.first, "pageLoadingTrident")
    }

    @Test
    fun `Loader FullScreen in loading state cas to  legacy Progress false`() {
        val loader = Loader.createFullScreen(indicator = TridentDefault, isLoading = true)
        val progress = loader.getLegacyProgress()
        assertEquals(progress.first, "")
        assertFalse(progress.second)
    }

    @Test
    fun `Loader Component in loading state cast to  legacy Content true`() {
        val loader = Loader.createComponent(key = "key", isLoading = true)
        val content = loader.getLegacyContentLoaded()
        assertEquals(content.first, "")
        assertTrue(content.second)
    }


}