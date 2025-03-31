package com.example.uspower.di

import com.example.uspower.features.auth.di.authModule
import com.example.uspower.features.chat.chatroom.di.chatRoomModule
import com.example.uspower.features.chat.di.chatModule
import com.example.uspower.features.chat.editchat.addmembers.di.addMembersToChatModule
import com.example.uspower.features.chat.editchat.di.editChatModule
import com.example.uspower.features.createchat.addandcreate.di.addAndCreateChatModule
import com.example.uspower.features.createchat.addusers.di.addUsersToChatModule
import com.example.uspower.features.createchat.chatname.di.chatNameModule
import com.example.uspower.features.createchat.create.di.createChatComponentModule
import com.example.uspower.features.createchat.di.addNewChatModule
import com.example.uspower.features.editprofile.di.editprofileModule
import com.example.uspower.features.mainflow.di.mainFlowModule
import com.example.uspower.features.profileflow.createprofile.di.createProfileModule
import com.example.uspower.features.tabflow.di.tabModule
import com.example.uspower.features.profileflow.profile.di.profileModule
import com.example.uspower.features.root.di.rootModule
import com.example.uspower.features.singleuser.di.singleUserModule
import com.example.uspower.features.splash.di.splashModule
import com.example.uspower.features.userlist.di.usersModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            sharedModule,
            platformModule,
            authModule,
            rootModule,
            chatModule,
            splashModule,
            profileModule,
            usersModule,
            tabModule,
            chatRoomModule,
            mainFlowModule,
            singleUserModule,
            editprofileModule,
            addNewChatModule,
            editChatModule,
            addAndCreateChatModule,
            addUsersToChatModule,
            createChatComponentModule,
            chatNameModule,
            addMembersToChatModule,
            createProfileModule,
            supabaseModule
        )
    }
}