package com.fachhochschulebib.fhb.pruefungsplaner.utils

import android.util.Log
import com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry
import java.text.SimpleDateFormat
import java.util.*

/**
 * Inner Class to filter the table of modules.
 *
 * @author Alexander Lange
 * @since 1.6
 */
object Filter {
    /**
     * Parameter to Filter with the module name.
     * Calls the [onFilterChangedListener].
     *
     * @author Alexander Lange
     * @since 1.6
     *
     * @see onFilterChangedListener
     */
    var modulName: String? = null
        set(value) {
            field = value
            filterChanged()
        }

    /**
     * Parameter to Filter with the course name.
     * Calls the [onFilterChangedListener].
     *
     * @author Alexander Lange
     * @since 1.6
     *
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
     * Calls the [onFilterChangedListener].
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
    @Suppress("SetterBackingFieldAssignment", "UNUSED_PARAMETER")
    var semester: Array<Boolean> = arrayOf(true, true, true, true, true, true)
        set(value) {
            return
        }

    /**
     * Public method to set the value for a specific semester.
     * Calls the [onFilterChangedListener]
     * @param[pSemester] The semester to set the value.
     * @param[active] If the semester is checked or not.
     * @author Alexander Lange
     * @since 1.6
     *
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
     * Validates a [TestPlanEntry]-Object. Checks if all Filter-values agree with the given entry.
     *
     * @param[entry] The Entry that needs to be validated
     * @return true->The entry agrees with the filter,false->the entry does not agree with the filter
     * @author Alexander Lange
     * @since 1.6
     */
    @Suppress("DEPRECATION")
    private fun validateFilter(entry: TestPlanEntry?): Boolean {
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
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = entry.date?.let { sdf.parse(it) }
            val comp = date?.date?.let { Date(date.year, date.month, it,0,0,0) }?:return false
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
