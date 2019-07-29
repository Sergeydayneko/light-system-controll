package ru.dayneko.webservice.lightSystem.utils

import java.lang.NumberFormatException
import java.util.function.BiPredicate

/**
 * Функция для преобразования состояния света на противоположенный
 */
fun Int.reverseState() = when(this) {
                            1 -> 0
                            0 -> 1
                            else -> throw NumberFormatException("Wrong number for reversing integer light state")
                        }

/**
 * Возвращает хвостовую часть коллекции
 * Property Extension
 *
 * @return хвостовую часть коллекции
 */
val <T> List<T>.tail: List<T>
        get() = drop(1)

/**
 * Возвращает голову коллекции
 * Property Extension
 *
 * @return голова коллекции
 */
val <T> List<T>.head: T
    get() = first()

/**
 * Линейный комбинатор
 */
infix fun <A, B> A.andThen(func: (A) -> B): B = func(this)

/**
 * Функция создания попарных значений через предикат
 */
fun <A, C, L>Iterable<A>.zipByPredicate(
          zipCollection: Iterable<C>
        , predicate: (arg1: A, arg2: C) -> Boolean
        , transform: (arg1: A, Arg2: C) -> L): List<L> {
    val resultList = mutableListOf<L>()
    for (a in this) {
        for (b in zipCollection) {
            if (predicate(a, b)) {
                resultList.add(transform(a, b))
            }
        }
    }
    return resultList
}

/**
 * Функция нахождение пересечения двух коллекций по предикату
 */
fun <I, Z>Iterable<I>.crossBy(crossCollection: Iterable<Z>, crossPredicate: (I, Iterable<Z>) -> Boolean): MutableList<I> {
    val resultList = mutableListOf<I>()
    for (el in this) {
        if (crossPredicate(el, crossCollection)) {
            resultList.add(el)
        }
    }
    return resultList
}