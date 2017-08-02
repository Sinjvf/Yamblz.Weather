package ru.exwhythat.yather.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by exwhythat on 01.08.17.
 */

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface DataScope {
}
