package com.example.musfeat.architecture

import moxy.MvpPresenter

open class BasePresenter<V: BaseView> : MvpPresenter<V>()