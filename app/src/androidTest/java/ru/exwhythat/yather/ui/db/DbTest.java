package ru.exwhythat.yather.ui.db;

import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;

import ru.exwhythat.yather.db.YatherDb;

/**
 * Created by exwhythat on 8/4/17.
 */

abstract public class DbTest {
    protected YatherDb db;

    @Before
    public void initDb() {
        db = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getTargetContext(),
                YatherDb.class).build();
    }

    @After
    public void closeDb() {
        db.close();
    }
}
