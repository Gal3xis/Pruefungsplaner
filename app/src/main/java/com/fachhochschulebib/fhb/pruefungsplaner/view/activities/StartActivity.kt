package com.fachhochschulebib.fhb.pruefungsplaner.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fachhochschulebib.fhb.pruefungsplaner.R
import com.fachhochschulebib.fhb.pruefungsplaner.model.room.Faculty
import com.fachhochschulebib.fhb.pruefungsplaner.utils.applySettings
import com.fachhochschulebib.fhb.pruefungsplaner.view.helper.CoursesCheckList
import com.fachhochschulebib.fhb.pruefungsplaner.viewmodel.StartViewModel
import com.fachhochschulebib.fhb.pruefungsplaner.viewmodel.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import kotlinx.android.synthetic.main.activity_change_faculty.*

/**
 * Unique code for the update requests.
 * @see StartActivity.initUpdateManager
 */
const val UPDATE_REQUEST_CODE = 100

/**
 * String constant that is used as extra in the intent to notify that the Start Activity shall not skip if a selected main course is recognized.
 * Used for changing the faculty.
 */
const val CHANGE_FLAG = "changeFlag"

/**
 * Activity, that allows the user to pick a faculty and select courses.
 * First activity called on app start and can also be opened from navigationdrawer in the [MainActivity].
 * Also initializes a background worker to look for new Database-updates and an Updatemanager to look for new App updates.
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
 */
@Suppress("DEPRECATION")
class StartActivity : AppCompatActivity() {

    /**
     * ViewModel for the StartActivity. Is set in [onCreate].
     * @see StartViewModel
     */
    private lateinit var viewModel: StartViewModel

    /**
     * The recyclerview to display all courses for a selected faculty, from where the user can pick his courses.
     */
    private lateinit var recyclerViewCourses: CoursesCheckList


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
        setContentView(R.layout.activity_change_faculty)
        initRecyclerviewCourses()
        initButtons()
        viewModel.fetchFaculties()
        val changeFlag = intent.getBooleanExtra(CHANGE_FLAG, false)
        if (changeFlag) {
            return
        }
        if (viewModel.checkLoginStatus()) {
            startApplication()
        }
    }


    /**
     * Initializes the recyclerview that shows all courses for the selected faculty.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun initRecyclerviewCourses() {
        activity_change_faculty_recyclerview_courses.setHasFixedSize(true)
        activity_change_faculty_recyclerview_courses.layoutManager = LinearLayoutManager(applicationContext)
        recyclerViewCourses = CoursesCheckList(listOf(), viewModel, applicationContext)
        activity_change_faculty_recyclerview_courses.adapter = recyclerViewCourses
        viewModel.liveCoursesForFaculty.observe(this) { items ->
            if (items != null) {
                activity_change_faculty_label_select_courses.visibility = View.VISIBLE
                recyclerViewCourses.updateContent(items)
                return@observe
            }
            activity_change_faculty_label_select_courses.visibility = View.GONE
        }

    }

    /**
     * Initializes the buttons of the UI.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun initButtons() {
        activity_change_faculty_button_select_faculty.setOnClickListener {
            clickedChooseFaculty()
        }
        activity_change_faculty_button_ok.setOnClickListener {
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
     * @author Alexander Lange
     * @since 1.6
     */
    private fun clickedOk() {
        val chosen = recyclerViewCourses.getChosen()
        if (chosen.isNotEmpty()) {
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
                chooseCourse.setTitle(R.string.change_faculty_choose_main_course)
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
     * Opens the [MainActivity].
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun startApplication() {
        val mainWindow = Intent(applicationContext, MainActivity::class.java)
        startActivityForResult(mainWindow, 0)
    }

    /**
     * Called, when the user picked a faculty.
     * Fills the recyclerview with courses.
     *
     * @param faculty The chosen faculty
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun facultyChosen(faculty: Faculty) {
        activity_change_faculty_button_select_faculty.text = faculty.facultyName
        viewModel.setSelectedFaculty(faculty)
    }
}