package com.example.dogga;


import static org.mockito.Mockito.*;
import org.junit.*;
import java.io.FileNotFoundException;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class testScanISBN {
    private final NetworkUtilis doublureNetworkUtilis = spy(new NetworkUtilis());

    public testScanISBN() {
    }

    @Test
    public void testScanBookISBN() {
        String zelda_TP_Manga_Tome_10 = "2302096991";
        when(NetworkUtilis.getBookInfo(zelda_TP_Manga_Tome_10)).thenReturn("Yes");

        NetworkUtilis.getBookInfo(zelda_TP_Manga_Tome_10);

    }
}