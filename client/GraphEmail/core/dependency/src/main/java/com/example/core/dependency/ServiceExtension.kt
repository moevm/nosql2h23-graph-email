package com.example.core.dependency

import android.app.Service

internal val Service.depsApplication: HasDependencies?
    get() = application as? HasDependencies

//Dependencies
inline fun <reified D : Dependencies> Service.findDependencies(): D {
    return findDependenciesByClass(D::class.java)
}

@Suppress("UNCHECKED_CAST")
fun <D : Dependencies> Service.findDependenciesByClass(clazz: Class<D>): D {
    return depsApplication?.depsMap?.get(clazz) as? D ?: throw IllegalStateException("No Dependencies $clazz in application")
}