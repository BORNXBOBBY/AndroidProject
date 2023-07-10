package com.example.audiox

object Constants {
    fun getEmployeeData():ArrayList<DataModel>{

        val employeeList=ArrayList<DataModel>()
        val emp1=DataModel("Arjit Singh","Artist")
        employeeList.add(emp1)
        val emp2=DataModel("Annie marrie","Singer")
        employeeList.add(emp2)
        val emp3=DataModel("Darshan Raval","Music Singer")
        employeeList.add(emp3)
        val emp4=DataModel("Arman Malik","Composer")
        employeeList.add(emp4)
        val emp5=DataModel("Salena Gomez","Director, Singer")
        employeeList.add(emp5)
        return  employeeList
    }

}