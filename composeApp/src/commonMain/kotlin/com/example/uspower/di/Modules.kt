package com.example.uspower.di

import com.example.uspower.core.chat.ChatRepository
import com.example.uspower.core.chat.ChatRepositoryImpl
import com.example.uspower.core.messages.MessageRepository
import com.example.uspower.core.messages.MessageRepositoryImpl
import com.example.uspower.core.login.LoginManager
import com.example.uspower.core.login.LoginManagerSupabase
import com.example.uspower.core.messages.MessageCache
import com.example.uspower.core.messages.MessageProvider
import com.example.uspower.core.messages.MessageProviderImpl
import com.example.uspower.core.messages.realtime.MessageRealTimeProvider
import com.example.uspower.core.messages.realtime.MessageRealTimeProviderImpl
import com.example.uspower.core.users.UsersRepository
import com.example.uspower.core.users.UsersRepositoryImpl
import com.example.uspower.data.api.chats.supabase.SupabaseChatApi
import com.example.uspower.data.api.files.CommonUploader
import com.example.uspower.data.api.files.FilesApi
import com.example.uspower.data.api.files.FilesApiImpl
import com.example.uspower.data.api.messages.MessagesApi
import com.example.uspower.data.api.messages.MessagesSupabaseApiImpl
import com.example.uspower.data.api.users.SupabaseUsersApiImpl
import com.example.uspower.data.api.users.UsersApi
import com.example.uspower.data.datastore.LocalPreferences
import com.example.uspower.data.datastore.LocalPreferencesImpl
import com.example.uspower.notifications.NotificationSender
import com.example.uspower.notifications.NotificationSenderImpl
import com.example.uspower.notifications.NotificationSubscriber
import com.example.uspower.notifications.NotificationSubscriberImpl
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.logging.LogLevel
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module


val sharedModule = module {
    single {
        UsersRepositoryImpl(get(), get(), get())
    }.bind<UsersRepository>()

    single {
        SupabaseUsersApiImpl(get())
    }.bind<UsersApi>()

    single {
        MessagesSupabaseApiImpl(get())
    }.bind<MessagesApi>()

    single {
        FilesApiImpl(get())
    }.bind<FilesApi>()

    single {
        LocalPreferencesImpl(get())
    }.bind<LocalPreferences>()


    single {
        LoginManagerSupabase(get(), get(), get())
    }.bind<LoginManager>()

    single {
        ChatRepositoryImpl(get(), get())
    }.bind<ChatRepository>()

    single {
        MessageRepositoryImpl(get(), get(), get())
    }.bind<MessageRepository>()


    single {
        MessageCache()
    }

    single {
        MessageProviderImpl(get(), get(),    get(), get())
    }.bind<MessageProvider>()

    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
    }

    single {
        NotificationSenderImpl(get())
    }.bind<NotificationSender>()

    single {
        NotificationSubscriberImpl(get())
    }.bind<NotificationSubscriber>()

    single {
        CommonUploader()
    }

    single {
        com.example.uspower.data.api.chats.supabase.SupabaseChatApiImpl(get())
    }.bind<SupabaseChatApi>()


    single {
        CoroutineScope(
            Dispatchers.IO + CoroutineExceptionHandler { _, e ->
                println("100500 Exception in root scope $e, ${e.printStackTrace()}")
            }
        )
    }

    single(createdAtStart = false) {
        MessageRealTimeProviderImpl(get(), get(), get(), get(), get())
    }.bind<MessageRealTimeProvider>()


}


val supabaseModule = module {
    single {
        createSupabaseClient(
            supabaseUrl = "https://supbase.peinlab.pp.ua/",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.ewogICJyb2xlIjogInNlcnZpY2Vfcm9sZSIsCiAgImlzcyI6ICJzdXBhYmFzZSIsCiAgImlhdCI6IDE3MzkzMTEyMDAsCiAgImV4cCI6IDE4OTcwNzc2MDAKfQ.b9cGPza1xiC68PfxTTmJ1gVF-GNtWlEk3GSqxI1r-Zc"
//            supabaseUrl = "https://mrhlekvbxrgkmwhblgxd.supabase.co",
//            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im1yaGxla3ZieHJna213aGJsZ3hkIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTc0MjgwNzUzMiwiZXhwIjoyMDU4MzgzNTMyfQ.lp-59oejBDE2akniUerHJhuh14q5ze-8vHmT6Wuxk-c"
        ) {
            defaultLogLevel = LogLevel.NONE
            install(Postgrest)
            install(Auth)
            install(Realtime)
        }
    }
}
