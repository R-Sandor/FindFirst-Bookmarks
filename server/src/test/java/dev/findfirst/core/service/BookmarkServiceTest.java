package dev.findfirst.core.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dev.findfirst.core.model.jdbc.BookmarkJDBC;
import dev.findfirst.core.repository.jdbc.BookmarkJDBCRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookmarkServiceTest {

  @InjectMocks
  private BookmarkService bookmarkService;
  @Mock
  private BookmarkJDBCRepository bookmarkRepository;
  @Mock
  private ScreenshotManager sManager;

  /**
   * Tests that addMissingScreenShotUrlToBookMarks method of BookmarkService class add screenshots
   * urls to scrapable bookmarks.
   * 
   * @param scrapable
   */
  @ParameterizedTest
  @NullSource
  @ValueSource(booleans = {true, false})
  void addMissingScreenShotUrlToBookMarksTests(Boolean scrapable) {
    String screenshotUrl = "http://example.com/3";
    BookmarkJDBC bookmark = new BookmarkJDBC();
    bookmark.setScrapable(scrapable);
    List<BookmarkJDBC> list = new ArrayList<>();
    list.add(bookmark);
    if (Boolean.TRUE.equals(scrapable)) {
      when(sManager.getScreenshot(any())).thenReturn(Optional.of(screenshotUrl));
      when(bookmarkRepository.saveAll(any())).thenReturn(list);
    }
    when(bookmarkRepository.findBookmarksWithEmptyOrBlankScreenShotUrl()).thenReturn(list);
    bookmarkService.addMissingScreenShotUrlToBookMarks();
    if (Boolean.TRUE.equals(scrapable)) {
      Assertions.assertEquals(screenshotUrl, bookmark.getScreenshotUrl(), "URL is not updated");
      verify(sManager, times(1)).getScreenshot(any());
    } else {
      Assertions.assertNull(bookmark.getScreenshotUrl(), "URL is updated");
      verify(sManager, never()).getScreenshot(any());
    }
    verify(bookmarkRepository, times(1)).findBookmarksWithEmptyOrBlankScreenShotUrl();
    verify(bookmarkRepository, times(1)).saveAll(list);
  }
}
