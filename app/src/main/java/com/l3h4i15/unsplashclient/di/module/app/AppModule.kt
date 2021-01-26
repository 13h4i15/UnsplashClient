package com.l3h4i15.unsplashclient.di.module.app

import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.l3h4i15.unsplashclient.converter.entity.EntityConverter
import com.l3h4i15.unsplashclient.converter.response.ApiConverter
import com.l3h4i15.unsplashclient.converter.entity.CollectionRelationConverter
import com.l3h4i15.unsplashclient.converter.entity.UserEntityConverter
import com.l3h4i15.unsplashclient.converter.response.CollectionResponseToModelConverter
import com.l3h4i15.unsplashclient.converter.response.PictureResponseToModelConverter
import com.l3h4i15.unsplashclient.converter.response.UserResponseToModelConverter
import com.l3h4i15.unsplashclient.db.entity.UserEntity
import com.l3h4i15.unsplashclient.db.relation.CollectionWithUserRelation
import com.l3h4i15.unsplashclient.db.repository.CollectionsRepository
import com.l3h4i15.unsplashclient.db.repository.CollectionsRepositoryImpl
import com.l3h4i15.unsplashclient.di.mapkey.ViewModelKey
import com.l3h4i15.unsplashclient.di.module.activity.ActivityModule
import com.l3h4i15.unsplashclient.di.module.activity.NavigationModule
import com.l3h4i15.unsplashclient.di.scope.ActivityScope
import com.l3h4i15.unsplashclient.factory.DaggerFragmentFactory
import com.l3h4i15.unsplashclient.ui.collections.CollectionsViewModel
import com.l3h4i15.unsplashclient.ui.random.RandomPictureViewModel
import com.l3h4i15.unsplashclient.factory.DaggerViewModelFactory
import com.l3h4i15.unsplashclient.model.Collection
import com.l3h4i15.unsplashclient.model.Picture
import com.l3h4i15.unsplashclient.model.User
import com.l3h4i15.unsplashclient.network.model.content.CollectionApiResponse
import com.l3h4i15.unsplashclient.network.model.content.PictureApiResponse
import com.l3h4i15.unsplashclient.network.model.user.UserResponse
import com.l3h4i15.unsplashclient.network.repository.UnsplashApiRepository
import com.l3h4i15.unsplashclient.network.repository.UnsplashApiRepositoryImpl
import com.l3h4i15.unsplashclient.ui.collectionpictures.CollectionPicturesViewModel
import com.l3h4i15.unsplashclient.ui.main.MainActivity
import com.l3h4i15.unsplashclient.ui.search.SearchResultViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class AppModule {
    //Injectors
    @ActivityScope
    @ContributesAndroidInjector(modules = [ActivityModule::class, NavigationModule::class])
    abstract fun contributeMainActivity(): MainActivity

    // Fragments
    @Binds
    abstract fun bindFragmentFactory(factory: DaggerFragmentFactory): FragmentFactory

    // View models
    @Binds
    abstract fun bindViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(RandomPictureViewModel::class)
    abstract fun bindRandomPictureViewModel(viewModel: RandomPictureViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CollectionsViewModel::class)
    abstract fun bindCollectionsViewModel(viewModel: CollectionsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CollectionPicturesViewModel::class)
    abstract fun bindCollectionPicturesViewModel(viewModel: CollectionPicturesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchResultViewModel::class)
    abstract fun bindSearchResultViewModel(viewModel: SearchResultViewModel): ViewModel

    // Repositories
    @Binds
    abstract fun bindUnsplashApiRepository(repository: UnsplashApiRepositoryImpl): UnsplashApiRepository

    @Binds
    abstract fun bindCollectionsRepository(repository: CollectionsRepositoryImpl): CollectionsRepository

    // Converters
    @Binds
    abstract fun bindCollectionRelationConverter(converter: CollectionRelationConverter): EntityConverter<Collection, CollectionWithUserRelation>

    @Binds
    abstract fun bindUserEntityConverter(converter: UserEntityConverter): EntityConverter<User, UserEntity>

    @Binds
    abstract fun bindUserResponseToModelConverter(converter: UserResponseToModelConverter): ApiConverter<UserResponse, User>

    @Binds
    abstract fun bindPictureResponseToModelConverter(converter: PictureResponseToModelConverter): ApiConverter<PictureApiResponse, Picture>

    @Binds
    abstract fun bindCollectionResponseToModelConverter(converter: CollectionResponseToModelConverter): ApiConverter<CollectionApiResponse, Collection>
}