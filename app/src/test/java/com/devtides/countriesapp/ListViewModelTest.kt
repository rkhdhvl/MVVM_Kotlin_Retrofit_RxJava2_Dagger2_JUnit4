package com.devtides.countriesapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.devtides.countriesapp.model.CountriesService
import com.devtides.countriesapp.model.Country
import com.devtides.countriesapp.viewmodel.ListViewModel
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

class ListViewModelTest {

    /*
    * Reference Articles
    * https://medium.com/@peter.tackage/overriding-rxandroid-schedulers-in-rxjava-2-5561b3d14212
    * https://stackoverflow.com/questions/16467685/difference-between-mock-and-injectmocks
    * */

    /*
    * InstantTaskExecutorRule is a JUnit Test Rule that swaps the background executor used by
    * the Architecture Components with a different one which executes each task synchronously*/

    @Mock
    lateinit var countriesService: CountriesService

    @InjectMocks
    var listViewModel = ListViewModel()

    private var testSingle : Single<List<Country>>?=null

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Before
    fun setup()
    {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun getCountriesTest()
    {
        // given
        // creating some mock data
        val country = Country("countryName","countryCapital","url")
        val countriesList = arrayListOf<Country>(country)
        testSingle = Single.just(countriesList)

        // when
        Mockito.`when`(countriesService.getCountries()).thenReturn(testSingle)
        // testing the real functionality
        listViewModel.refresh()

        // then
        assertEquals(1,listViewModel.countries.value?.size)
        assertEquals(false,listViewModel.countryLoadError.value)
        assertEquals(false,listViewModel.loading.value)
    }

    @Test
    fun getCountriesError()
    {
        // given
        testSingle = Single.error(Throwable())
        // when
        Mockito.`when`(countriesService.getCountries()).thenReturn(testSingle)
        listViewModel.refresh()
        // then
        assertEquals(true,listViewModel.countryLoadError.value)
        assertEquals(false,listViewModel.loading.value)
    }

    @Before
    fun setUpRxSchedulers()
    {
        // declaring an annonymous object which extends the scheduler class and then overriding its methods
        // assigning a name to an anonymous and storing it in a variable
        val immediate = object : Scheduler() {

            // basically tells whenever an observable is called we need to return immediately
            override fun scheduleDirect(run: Runnable?, delay: Long, unit: TimeUnit?): Disposable {
                return super.scheduleDirect(run,0,unit)
            }

            override fun createWorker(): Worker {
                return ExecutorScheduler.ExecutorWorker(Executor { it.run() } )
            }
        }

        RxJavaPlugins.setInitIoSchedulerHandler { scheduler -> immediate}
        RxJavaPlugins.setInitComputationSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setInitNewThreadSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setInitSingleSchedulerHandler { scheduler -> immediate }
        /*
        * RxAndroid's RxAndroidPlugins class provides hooks for overriding RxAndroid's Schedulers */
        // Allows you to override the default scheduler instance
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler -> immediate }

    }


}