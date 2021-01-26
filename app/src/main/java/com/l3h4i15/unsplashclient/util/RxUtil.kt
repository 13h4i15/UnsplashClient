package com.l3h4i15.unsplashclient.util

import io.reactivex.rxjava3.disposables.Disposable

fun Disposable?.disposeIfPossible() {
    this ?: return
    if (!isDisposed) dispose()
}