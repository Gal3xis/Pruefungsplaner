package com.Fachhochschulebib.fhb.pruefungsplaner

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.Fachhochschulebib.fhb.pruefungsplaner.model.RetrofitConnect
import android.content.Intent
import android.os.Bundle
import android.content.pm.ActivityInfo
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.Toast
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.Fachhochschulebib.fhb.pruefungsplaner.data.Courses
import com.Fachhochschulebib.fhb.pruefungsplaner.data.Faculty
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import org.json.JSONArray
import java.lang.Exception
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
class StartActivity() : AppCompatActivity() {
    var mCourses: CoursesCheckList? = null

    private var updateManager: AppUpdateManager? = null
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

    //KlassenVariablen
    private var courseMain: String? = null
    private var jsonArrayFacultys: JSONArray? = null
    val courseName: MutableList<String> = ArrayList()
    val facultyName: MutableList<String> = ArrayList()

    // private Spinner spStudiengangMain;
    // List<String> idList = new ArrayList<String>();
    private lateinit var context: Context
    private lateinit var viewModel: MainViewModel

    private fun addMainCourse(course: Courses) {
        course.courseName?.let { addMainCourse(it) }
    }

    // Start Merlin Gürtler
    // Fügt den Hauptstudiengang zu den Shared Preferences hinzu
    /**
     * Selects a course a the main course.
     *
     * @param[choosenCourse] The course that is supposed to be the mein course.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun addMainCourse(choosenCourse: String) {
        returnCourse = choosenCourse?.let { viewModel.getCourseId(it) }
        choosenCourse?.let { viewModel.setSelectedCourse(it) }
        returnCourse?.let { viewModel.setReturnCourse(it) }
        if (viewModel.getUuid() == null) {
            RetrofitConnect(context).firstStart()
        } else {
            RetrofitConnect(context).setUserCourses()
        }
    }

    // Schließt die App beim BackButton
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


    // Ende Merlin Gürtler
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
        applySettings()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start)
        context = baseContext
        viewModel = ViewModelProvider(
            this,
            MainViewModelFactory(application)
        )[MainViewModel::class.java]
        initUpdateManager()
        initSharedPreferences()
        checkLoginStatus()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        recyclerViewChecklist.setHasFixedSize(true)
        recyclerViewChecklist.layoutManager = LinearLayoutManager(applicationContext)

        //Defininition des Arrays jahreszeit
        val season: MutableList<String> = ArrayList()
        season.add(context?.getString(R.string.sommer).toString())
        season.add(context?.getString(R.string.winter).toString())

        //Kalender:: aktuelles Jahr --> Bestimmung der Prüfphase (WiSe, SoSe)
        val calendar = Calendar.getInstance()
        val calendarMonth = calendar[Calendar.MONTH]

        // Start Merlin Gürtler
        //Anzahl der Elemente
        //Adapter-Aufruf
        val strJson = viewModel.getCurrentPeriode()
        try {
            initServer()
            initButtons()
        } catch (e: Exception) {
            if (strJson != null) {
                try {
                    jsonArrayFacultys = JSONArray(strJson)
                    var i = 0
                    while (i < jsonArrayFacultys!!.length()) {
                        val json = jsonArrayFacultys!!.getJSONObject(i)
                        facultyName.add(json["facName"].toString())
                        initButtons()
                        i++
                    }
                } catch (b: Exception) {
                    Log.d("Datenbankfehler", "Keine Daten in der Datenbank vorhanden!")
                }
            }
        }
    }

    //TODO
    private fun checkLoginStatus() {
        Log.d("ReturnCourse",viewModel.getReturnCourse().toString())
        if(viewModel.getReturnCourse()!=null){
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

    /**
     * Initializes the sharedPrefernces and the parameter which are attatched to them.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun initSharedPreferences() {
        viewModel.setServerIPAddress(context.getString(R.string.server_adress))
        viewModel.setServerRelUrlPath(context.getString(R.string.server_url))
        courseMain = viewModel.getSelectedCourse()
    }

    //Aufruf in onCreate()
    /**
     * Equalizes the Room-Database with the Webserver.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun initServer() {
        viewModel.fetchFaculties()
        viewModel.fetchCourses()
        // Start Merlin Gürtler
        val globalVariable = applicationContext as StartClass
        // Thread für die UUid
        val uuid = viewModel.getUuid()
        if (!globalVariable.appStarted && (uuid != null) && !globalVariable.isChangeFaculty) {
            globalVariable.appStarted = true
            //TODO retrofit.anotherStart()
        }

    }

    /**
     * Initializes the buttons of the UI.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private fun initButtons() {
        buttonForSpinner.setOnClickListener {
            createAlertDialogChooseFaculty()
        }
        buttonOk.setOnClickListener { v ->
            createAlertDialogSelectMainCourse(v)
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
    private fun createAlertDialogSelectMainCourse(view: View) {
        val chooseCourse = AlertDialog.Builder(
            this@StartActivity,
            R.style.customAlertDialog
        )
        val checkList = (recyclerViewChecklist.adapter as CoursesCheckList)
        val chosen = checkList.getChosen()
        var oneFavorite = chosen.isNotEmpty()
        val courses = checkList.courseList
        if (oneFavorite) {
            courses.forEach {
                viewModel.updateCourse(it)
            }
        }
        if (oneFavorite) {
            if (chosen.size == 1) {
                addMainCourse(chosen[0])
                startApplication()
            } else {
                val stringCourses = mutableListOf<String>()
                chosen.forEach {
                    it.courseName?.let { it1 -> stringCourses.add(it1) }
                }
                chooseCourse.setTitle(R.string.choose_main)
                chooseCourse.setItems(
                    stringCourses.toTypedArray()
                ) { _, which ->
                    addMainCourse(stringCourses[which])
                    startApplication()
                }
                chooseCourse.show()
            }
        } else {
            Toast.makeText(
                view.context,
                view.context.getString(R.string.favorite_one_course),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun startApplication() {
        val mainWindow = Intent(applicationContext, MainActivity::class.java)
        startActivityForResult(mainWindow, 0)
    }

    /**
     * Creates a dialog that asks the user to select a faculty.
     * @author Alexander Lange
     * @since 1.6
     */
    private fun createAlertDialogChooseFaculty() {

        val chooseFaculty = AlertDialog.Builder(
            this@StartActivity,
            R.style.customAlertDialog
        )
        viewModel.liveFaculties.observe(this) { items ->
            val stringFaculties = mutableListOf<String>()
            items?.forEach {
                stringFaculties.add(it.facultyName)
            }
            chooseFaculty.setItems(
                stringFaculties.toTypedArray()
            ) { dialog, which ->
                items?.let { facultyChosen(it[which]) }
                dialog.dismiss()
            }
            chooseFaculty.create()
            chooseFaculty.show()
        }
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
        viewModel.fetchCourses()
        viewModel.liveCoursesForFacultyId?.observe(this) { items ->
            recyclerViewChecklist.adapter = items?.let {
                CoursesCheckList(
                    it,
                    viewModel,
                    applicationContext
                )
            }
        }

        /*
            for (i in facultyName.indices) {
                if ((faculties[which] == facultyName[i])
                ) {
                    try {
                        val `object` = jsonArrayFacultys!!.getJSONObject(i)
                        returnFaculty = `object`["fbid"].toString()
                        Log.d(
                            "Output Fakultaet",
                            returnFaculty ?: "No Faculty"
                        )
                        // Erstelle Shared Pref für die anderen Fragmente
                        returnFaculty?.let { viewModel.setReturnFaculty(it) }

                        // füllt die Liste mit Studiengängena
                        val courses = returnFaculty?.let { viewModel.getAllCoursesByFacultyid(it) }
                        courseChosen.clear()
                        courseName.clear()
                        if (courses != null) {
                            for (course in courses) {
                                courseName.add(course?.courseName ?: "")
                                courseChosen.add(
                                    course?.choosen ?: false
                                )
                            }
                        }
                        val faculty = faculties[which]
                            ?: "No Faculty"
                        if (faculty.contains(" ")) {
                            buttonForSpinner.text =
                                faculty
                                    .substring(
                                        0,
                                        faculty
                                            .indexOf(' ')
                                    )
                        } else {
                            buttonForSpinner.text = faculty
                        }

                        // füge den Adapter der Recyclerview hinzu
                        mAdapter = CheckListAdapter(
                            courseName,
                            courseChosen,
                            applicationContext,
                            viewModel
                        )
                        recyclerViewChecklist.adapter = mAdapter
                        if (chooseCourseId.visibility != View.VISIBLE) {
                            chooseCourseId.visibility =
                                View.VISIBLE
                        }
                        if (courseName.size == 0) {
                            if (buttonOk!!.visibility == View.VISIBLE) {
                                buttonOk!!.visibility =
                                    View.INVISIBLE
                            }
                            chooseCourseId.setText(R.string.no_course)
                            chooseCourseId.setTextColor(
                                getColorFromAttr(colorOnPrimary, theme)
                            )
                        } else {
                            if (buttonOk!!.visibility != View.VISIBLE) {
                                buttonOk!!.visibility =
                                    View.VISIBLE
                            }
                            chooseCourseId.setText(R.string.choose_course)
                            chooseCourseId.setTextColor(
                                getColorFromAttr(colorOnPrimary, theme)
                            )
                        }
                    } catch (e: Exception) {
                        Log.d(
                            "uebergabeAnSpinner",
                            "Fehler: Parsen von 'uebergabeAnSpinner'"
                        )
                    }
                } //if
            }*/
    }


    companion object {
        var returnCourse: String? = null
        var returnFaculty: String? = null
    }
}