package de.rki.coronawarnapp.ui.submission

import android.Manifest
import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import dagger.Module
import dagger.android.ContributesAndroidInjector
import de.rki.coronawarnapp.submission.SubmissionRepository
import de.rki.coronawarnapp.ui.submission.testresult.TestResultUIState
import de.rki.coronawarnapp.ui.submission.testresult.negative.SubmissionTestResultNegativeFragment
import de.rki.coronawarnapp.ui.submission.testresult.negative.SubmissionTestResultNegativeViewModel
import de.rki.coronawarnapp.util.DeviceUIState
import de.rki.coronawarnapp.util.NetworkRequestWrapper
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import kotlinx.coroutines.flow.flowOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import testhelpers.BaseUITest
import testhelpers.Screenshot
import testhelpers.SystemUIDemoModeRule
import testhelpers.TestDispatcherProvider
import testhelpers.captureScreenshot
import tools.fastlane.screengrab.locale.LocaleTestRule
import java.util.Date

@RunWith(AndroidJUnit4::class)
class SubmissionTestResultNegativeFragmentTest : BaseUITest() {

    lateinit var viewModel: SubmissionTestResultNegativeViewModel
    @MockK lateinit var submissionRepository: SubmissionRepository

    @Rule
    @JvmField
    val localeTestRule = LocaleTestRule()

    @get:Rule
    val systemUIDemoModeRule = SystemUIDemoModeRule()

    @get:Rule
    val grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)

        every { submissionRepository.deviceUIStateFlow } returns flowOf()
        every { submissionRepository.testResultReceivedDateFlow } returns flowOf()

        viewModel = spyk(
            SubmissionTestResultNegativeViewModel(
                TestDispatcherProvider,
                submissionRepository
            )
        )

        setupMockViewModel(object : SubmissionTestResultNegativeViewModel.Factory {
            override fun create(): SubmissionTestResultNegativeViewModel = viewModel
        })
    }

    @After
    fun teardown() {
        clearAllViewModels()
    }

    @Test
    @Screenshot
    fun capture_fragment() {
        every { viewModel.testResult } returns MutableLiveData(
            TestResultUIState(
                NetworkRequestWrapper.RequestSuccessful(
                    DeviceUIState.PAIRED_NEGATIVE
                ), Date()
            )
        )
        captureScreenshot<SubmissionTestResultNegativeFragment>()
    }
}

@Module
abstract class SubmissionTestResultTestNegativeModule {
    @ContributesAndroidInjector
    abstract fun submissionTestResultScreen(): SubmissionTestResultNegativeFragment
}