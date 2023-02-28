package org.jhoffmann.photostorybook.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class FileSystemTest {

    @Autowired
    FileSystem fileSystem;


    @BeforeEach
    void setUp() {
        fileSystem = new FileSystem(".");
    }

    @Test
    @DisplayName( "free disk space has to be positive" )
    void free_disk_space_has_to_be_positive() {


        // when
        long actual = fileSystem.getFreeDiskSpace();

        // then
        // assertTrue( actual > 0, "Free disk space was not > 0" );
        assertThat( actual ).isGreaterThan( 0 );
    }

    @Test
    void store_and_load_successful() throws IOException {

        fileSystem.store( "test.txt", "Hello World".getBytes() );
        byte[] actual = fileSystem.load( "test.txt" );
        assertThat( actual ).containsExactly( "Hello World".getBytes() );

        assertThatNoException().isThrownBy( () -> {
                Files.delete(Paths.get("test.txt"));
            });
    }

    @Test
    void load_unknown_file_throws_exception() {

        assertThatThrownBy( () -> {
            fileSystem.load( UUID.randomUUID().toString() );
        } ).isInstanceOf( UncheckedIOException.class );
    }

    @Test
    void load_arbitrary_file() throws IOException {

        assertThatThrownBy( () -> {
            fileSystem.load( "../../../../../../../../../../Windows/notepad.exe" );
        } ).isInstanceOf( SecurityException.class )
                .hasMessageContaining( "Access to" )
                .hasMessageContaining( "denied" );
    }
}