package com.example.efpro.notizen.models


import android.app.Application
/*This extention will be helpful to set the users thar are already logged in.*/
class ApplicationExt: Application() {

    companion object:IDList{

        override val contactlist: ArrayList<LocalUser> =  ArrayList()

        override fun add(element: LocalUser): Boolean {//add an element to the list
            val control =  analize(element)
            if(control==false){
                contactlist.add(element)//if it is not in the list yet, we add it.
            }
            return(control)
        }

        fun analize(element: LocalUser): Boolean {//We evaluete if the element is in the list
            var control = false
            for (item in contactlist){
                if(element.compare(item)){
                    control = true
                }
            }
            return control//devolvemos el resultado
        }

        override fun del(elementIndex: Int){//DELETE AN ELEMENT
            contactlist.removeAt(elementIndex)
        }
    }

}