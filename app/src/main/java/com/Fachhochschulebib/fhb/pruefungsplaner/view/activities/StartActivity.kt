package com.Fachhochschulebib.fhb.pruefungsplaner.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.Fachhochschulebib.fhb.pruefungsplaner.view.helper.CoursesCheckList
import com.Fachhochschulebib.fhb.pruefungsplaner.R
import com.Fachhochschulebib.fhb.pruefungsplaner.utils.applySettings
import com.Fachhochschulebib.fhb.pruefungsplaner.model.room.Faculty
import com.Fachhochschulebib.fhb.pruefungsplaner.viewmodel.ViewModelFactory
import com.Fachhochschulebib.fhb.pruefungsplaner.viewmodel.StartViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import java.util.*

//Alexander Lange Start
import kotlinx.android.synthetic.main.start.*
import kotlinx.coroutines.*

//Alexander Lange End

//////////////////////////////
// MainActivity
//
// autor:
// inhalt:  Auswahl des Studiengangs mit dazugehörigem PruefJahr und Semester
// zugriffsdatum: 11.12.19, 08/2020 (LG)
//
//////////////////////////////
/**
 * Activity, that allows the user to pick a faculty and select courses.
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
 */
class StartActivity : AppCompatActivity() {
    private lateinit var viewModel: StartViewModel
    private var updateManager: AppUpdateManager? = null
    private lateinit var recyclerViewCourses: CoursesCheckList

    private val UPDATE_REQUEST_CODE = 100

    private val installStateUpdateListener: InstallStateUpdatedListener =
            InstallStateUpdatedListener {
                if (it.installStatus() == InstallStatus.DOWNLOADED) {
                    Snackbar.make(
                            findViewById(android.R.id.content),
                            "Update is ready",
                            Snackbar.LENGTH_INDEFINITE
                    ).setAction("Install") {
                        updateManager?.completeUpdate()
                    }.show()
                }
            }

    /**
     * Overrides the onCreate()-Method, which is called first in the Fragment-LifeCycle.
     *
     * @since 1.6
     *
     * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
     *
     * @see Fragment.onCreate
     */

    public override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(application)
        )[StartViewModel::class.java]
        applySettings(viewModel)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start)


        initUpdateManager()
        initSharedPreferences()
        initRecyclerviewCourses()
        initButtons()
        if (viewModel.checkLoginStatus()) {
            startApplication()
        }
    }

    private fun initUpdateManager() {
        updateManager = AppUpdateManagerFactory.create(this)
        updateManager?.appUpdateInfo?.addOnSuccessListener {
            if (it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && it.isUpdateTypeAllowed(
                            AppUpdateType.FLEXIBLE
                    )
            ) {
                updateManager?.startUpdateFlowForResult(
                        it,
                        AppUpdateType.FLEXIBLE,
                        this,
                        UPDATE_REQUEST_CODE
                )
            }
        }
        updateManager?.registerListener(installStateUpdateListener)
    }

    private fun initRecyclerviewCourses() {
        recyclerViewChecklist.setHasFixedSize(true)
        recyclerViewChecklist.layoutManager = LinearLayoutManager(applicationContext)
        recyclerViewCourses = CoursesCheckList(listOf(),viewModel, applicationContext)
        recyclerViewChecklist.adapter = recyclerViewCourses
        viewModel.liveCoursesForFaculty.observe(this) { items ->
            if (items != null) {
                recyclerViewCourses.updateContent(items)
            }
        }

    }


    /**
     * Initializes the sharedPrefernces and the parameter which are attatched to them.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun initSharedPreferences() {
        viewModel.setServerIPAddress(applicationContext.getString(R.string.server_adress))
        viewModel.setServerRelUrlPath(applicationContext.getString(R.string.server_url))
    }


    /**
     * Initializes the buttons of the UI.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun initButtons() {
        buttonForSpinner.setOnClickListener {
            clickedChooseFaculty()
        }
        buttonOk.setOnClickListener {
            clickedOk()
        }
    }

    /**
     * Creates a dialog that asks the user to select a faculty.
     * @author Alexander Lange
     * @since 1.6
     */
    private fun clickedChooseFaculty() {
        viewModel.liveFaculties.observe(this) { faculties ->
            val stringFaculties = mutableListOf<String>()

            faculties?.forEach {
                stringFaculties.add(it.facultyName)
            }
            val chooseFaculty = AlertDialog.Builder(
                    this@StartActivity,
                    R.style.customAlertDialog
            )
            chooseFaculty.setItems(
                    stringFaculties.toTypedArray()
            ) { dialog, which ->
                faculties?.let { facultyChosen(it[which]) }
                dialog.dismiss()
            }
            chooseFaculty.create()
            chooseFaculty.show()
        }
    }

    /**
     * Creates a dialog that asks the User to select a main course.
     *
     * @param[view] The view that calls this method.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun clickedOk() {
        val chosen = recyclerViewCourses.getChosen()
        val courses = recyclerViewCourses.courseList
        if (chosen.isNotEmpty()) {
            courses.forEach {
                viewModel.updateCourse(it)
            }
            if (chosen.size == 1) {
                viewModel.addMainCourse(chosen[0])
                startApplication()
            } else {
                val stringCourses = mutableListOf<String>()
                chosen.forEach {
                    it.courseName.let { it1 -> stringCourses.add(it1) }
                }
                val chooseCourse = AlertDialog.Builder(
                        this@StartActivity,
                        R.style.customAlertDialog
                )
                chooseCourse.setTitle(R.string.choose_main)
                chooseCourse.setItems(
                        stringCourses.toTypedArray()
                ) { _, which ->
                    viewModel.addMainCourse(stringCourses[which])
                    startApplication()
                }
                chooseCourse.show()
            }
        }
    }

    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param[requestCode] – The integer request code originally supplied to startActivityForResult(), allowing you to identify who this result came from.
     * @param[resultCode] – The integer result code returned by the child activity through its setResult().
     * @param[data] – An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
     *
     * @author Alexander Lange
     * @since 1.6
     *
     * @see AppCompatActivity.onActivityResult
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UPDATE_REQUEST_CODE && resultCode != RESULT_OK) {
            Toast.makeText(this, "Cancel", Toast.LENGTH_LONG).show()
        }
        if (resultCode == 0) {
            finish()
        }
    }


    /**
     * Called when the app is stopped.
     *
     * @author Alexander Lange
     * @since 1.6
     *
     * @see AppCompatActivity.onStop
     */
    override fun onStop() {
        updateManager?.unregisterListener(installStateUpdateListener)
        super.onStop()
    }


    private fun startApplication() {
        val mainWindow = Intent(applicationContext, MainActivity::class.java)
        startActivityForResult(mainWindow, 0)
    }


    /**
     * Called, when the user picked a faculty.
     * Fills the recyclerview with courses.
     *
     * @param[faculties] The list of faculties.
     * @param[which] The index of the picked item.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun facultyChosen(faculty: Faculty) {
        viewModel.setReturnFaculty(faculty)
    }

}