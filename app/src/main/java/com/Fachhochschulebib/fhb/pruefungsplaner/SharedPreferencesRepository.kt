package com.Fachhochschulebib.fhb.pruefungsplaner

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesRepository(application: Application) {

    private val context = application.applicationContext

    private val validation = context.getSharedPreferences("validation", Context.MODE_PRIVATE)
    private val settings = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    private val serverAddress = context.getSharedPreferences("Server_Address", Context.MODE_PRIVATE)
    private val faculty = context.getSharedPreferences("faculty",Context.MODE_PRIVATE)
    private val examineTermin = context.getSharedPreferences("examineTermin",Context.MODE_PRIVATE)
    private val currentPeriode = context.getSharedPreferences("currentPeriode",Context.MODE_PRIVATE)


    fun getSelectedCourse():String?{
        return validation.getString("selectedCourse",null)
    }

    fun setSelectedCourse(course:String){
        val editor = validation.edit()
        editor.putString("selectedCourse",course)
        editor.apply()
    }

    fun getReturnCourse():String?{
        return validation.getString("returnCourse",null)
    }

    fun setReturnCourse(course: String){
        val editor = validation.edit()
        editor.putString("returnCourse",course)
        editor.apply()
    }

    fun getReturnFaculty():String?{
        return validation.getString("returnFaculty",null)
    }

    fun setReturnFaculty(faculty:String){
        val editor = validation.edit()
        editor.putString("returnFaculty",faculty)
        editor.apply()
    }

    fun getExamineYear():String?{
        return validation.getString("examineYear",null)
    }

    fun setExamineYear(year:String){
        val editor = validation.edit()
        editor.putString("examineYear",year)
        editor.apply()
    }

    fun getCurrentPeriode():String?{
        return validation.getString("currentPeriode",null)
    }

    fun setCurrentPeriode(periode:String){
        val editor = validation.edit()
        editor.putString("currentPeriode",periode)
        editor.apply()
    }

    fun getCurrentTermin():String?{
        return examineTermin.getString("currentTermin",null)
    }

    fun setCurrentTermin(termin:String){
        val editor = examineTermin.edit()
        editor.putString("currentTermin",termin)
        editor.apply()
    }

    //TODO Needed?
    fun getCurrentPeriodeString():String?{
        return currentPeriode.getString("currentPeriode",null)
    }

    fun setCurrentPeriodeString(str:String){
        val editor = currentPeriode.edit()
        editor.putString("currentPeriode",str)
        editor.apply()
    }

    fun getStartDate():String?{
        return currentPeriode.getString("startDate",null)
    }

    fun setStartDate(date:String){
        val editor = currentPeriode.edit()
        editor.putString("startDate",date)
        editor.apply()
    }

    fun getEndDate():String?{
        return currentPeriode.getString("endDate",null)
    }

    fun setEndDate(date: String){
        val editor = currentPeriode.edit()
        editor.putString("endDate",date)
        editor.apply()
    }

    //Returns false by default
    fun getChosenDarkmode():Boolean{
        return settings.getBoolean("darkmode",false)
    }

    fun setChosenDarkmode(darkmode:Boolean){
        val editor = settings.edit()
        editor.putBoolean("darkmode",darkmode)
        editor.apply()
    }

    //Returns -1 by default
    fun getChosenThemeId():Int{
        return settings.getInt("themeid",-1)
    }

    fun setChosenThemeId(id:Int){
        val editor = settings.edit()
        editor.putInt("themeid",id)
        editor.apply()
    }

    fun getUpdateIntervalTimeHour():Int{
        return settings.getInt("update_intervall_time_hour",0)
    }

    fun setUpdateIntervalTimeHour(hour:Int){
        val editor = settings.edit()
        editor.putInt("update_intervall_time_hour",hour)
        editor.apply()
    }

    //Return 15 by default
    fun getUpdateIntervalTimeMinute():Int{
        return settings.getInt("update_intervall_time_minute",15)
    }

    fun setUpdateIntervalTimeMinute(minute:Int){
        val editor = settings.edit()
        editor.putInt("update_intervall_time_minute",minute)
        editor.apply()
    }

    fun getCalendarSync():Boolean{
        return settings.getBoolean("calSync",false)
    }

    fun setCalendarSync(sync:Boolean){
        val editor = settings.edit()
        editor.putBoolean("calSync",sync)
        editor.apply()
    }

    fun getServerIPAddress():String?{
        return serverAddress.getString("ServerIPAddress",null)
    }

    fun setServerIPAddress(address:String){
        val editor = serverAddress.edit()
        editor.putString("ServerIPAddress",address)
        editor.apply()
    }

    fun getServerRelUrlPath():String?{
        return serverAddress.getString("ServerRelUrlPath",null)
    }

    fun setServerRelUrlPath(path:String){
        val editor = serverAddress.edit()
        editor.putString("ServerRelUrlPath",path)
        editor.apply()
    }

    fun getFaculties():String?{
        return faculty.getString("faculty",null)
    }

    fun setFaculties(faculties:String){
        val editor = faculty.edit()
        editor.putString("faculty",faculties)
        editor.apply()
    }

}