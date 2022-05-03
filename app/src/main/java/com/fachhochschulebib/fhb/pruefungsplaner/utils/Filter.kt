package com.fachhochschulebib.fhb.pruefungsplaner.utils

import android.util.Log
import com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry
import java.text.SimpleDateFormat
import java.util.*

/**
 * Inner Class to filter the table of moduls. Used by TermineFragment-fragment and FavoritenFragment-fragment.
 *
 * @author Alexander Lange
 * @since 1.6
 * @see Terminefragment
 * @see Favoritenfragment
 */
object Filter {
    /**
     * Parameter to Filter with the Modulename.
     * Calls the [onModuleNameChangedListener] and the [onFilterChangedListener].
     *
     * @author Alexander Lange
     * @since 1.6
     * @see onModulNameChangedListener
     * @see onFilterChangedListener
     */
    var modulName: String? = null
        set(value) {
            field = value
            filterChanged()
        }

    /**
     * Parameter to Filter with the Coursename.
     * Calls the [onCourseNameChangedListener] and the [onFilterChangedListener].
     *
     * @author Alexander Lange
     * @since 1.6
     * @see onCourseNameChangedListener
     * @see onFilterChangedListener
     */
    var courseName: String? = null
        set(value) {
            field = value
            filterChanged()
        }

    /**
     * Parameter to Filter with a specific date.
     * Calls the  [onFilterChangedListener].
     *
     * @author Alexander Lange
     * @since 1.6
     *
     * @see onFilterChangedListener
     */
    var datum: Date? = null
        set(value) {
            field = value
            filterChanged()
        }

    /**
     * Parameter to filter with a specific examiner.
     * Calls the [onExaminerChangedListener] and [onFilterChangedListener].
     *
     * @author Alexander Lange
     * @since 1.6
     *
     * @see onFilterChangedListener
     */
    var examiner: String? = null
        set(value) {
            field = value
            filterChanged()
        }

    /**
     * Array of 6 semester, where each field contains a boolean, if the semester is selected (true), or not (false)
     *
     * @author Alexander Lange
     * @since 1.6
     */
    var semester: Array<Boolean> = arrayOf(true, true, true, true, true, true)
        set(value) {
            return
        }

    /**
     * Public method to set the value for a specific semester.
     * Calls the [onSemesterChangedListener] and the [onFilterChangedListener]
     * @param[pSemester] The semester to set the value.
     * @param[active] If the semester is checked or not.
     * @author Alexander Lange
     * @since 1.6
     * @see onSemesterChangedListener
     * @see onFilterChangedListener

     */
    fun setSemester(pSemester: Int, active: Boolean) {
        semester[pSemester] = active
        filterChanged()
    }

    /**
     * Invokes every Method, appended to the onFilterChangedListener.
     *
     * @author Alexander Lange
     * @since 1.6
     * @see onFilterChangedListener
     */
    private fun filterChanged() {
        for (i in onFilterChangedListener) {
            i.invoke()
        }
        Log.d("Filter Changed\n",this.toString())
    }

    /**
     * Validates a testplanentry-Object. Checks if all Filter-values agree with the given entry.
     *
     * @param[context] Current context
     * @param[entry] The Entry that needs to be validated
     * @return true->The entry agrees with the filter,false->the entry does not agree with the filter
     * @author Alexander Lange
     * @since 1.6
     */
    fun validateFilter(entry: TestPlanEntry?): Boolean {
        if (entry == null) {
            return false
        }
        if (modulName != null && entry.module?.lowercase()?.startsWith(
                modulName?.lowercase() ?: entry.module?.lowercase() ?: "-1"
            ) == false
        ) {
            return false
        }
        if (entry.course?.lowercase()?.startsWith(
                courseName?.lowercase() ?: entry.course?.lowercase() ?: "-1"
            ) == false
        ) {
            return false
        }
        if (datum != null) {
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val date = sdf.parse(entry.date)
            val comp = Date(date.year,date.month,date.date,0,0,0)
            if (!datum!!.atDay(comp)) {
                return false
            }
        }
        if (entry.firstExaminer?.lowercase()?.startsWith(
                examiner?.lowercase() ?: entry.firstExaminer?.lowercase() ?: "-1"
            ) == false
        ) {
            return false
        }
        if (entry.semester != null && !semester[entry.semester!!.toInt().minus(1)]) {
            return false
        }
        return true
    }

    /**
     * Validates a list of [TestPlanEntry]-Objects. Checks if all Filter-values agree with the given entry.
     *
     * @param list The list, that needs to be checked
     *
     * @return A Filtered list with only [TestPlanEntry]-Objects, that fit to the current filter
     *
     *  @author Alexander Lange
     * @since 1.6
     */
    fun validateList(list: List<TestPlanEntry>): List<TestPlanEntry> {
        val ret = mutableListOf<TestPlanEntry>()
        list.forEach {
            if (validateFilter(it)) {
                ret.add(it)
            }
        }
        return ret
    }

    /**
     * Resets the Filter, sets every value to null.
     * Calls the onResetListener.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun reset() {
        courseName = null
        modulName = null
        datum = null
        semester.fill(true)
        examiner = null
    }

    /**
     * List of functions, that shall be invoked, when the filter changes.
     */
    var onFilterChangedListener: MutableList<() -> Unit> = mutableListOf()
}
