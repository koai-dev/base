package com.koai.example.contact

import com.koai.base.main.action.navigator.BaseNavigator
import com.koai.example.R
import com.koai.example.contact.screen.detailContact.DetailContactRouter
import com.koai.example.contact.screen.listContact.ListContactRouter

class ContactNavigator : BaseNavigator(), ContactRouter, DetailContactRouter, ListContactRouter {
    override fun goToListContact() {
        offNavScreen(R.id.action_global_listContactScreen)
    }

    override fun gotoDetailScreen() {
        offNavScreen(R.id.action_global_detailContactScreen)
    }
}