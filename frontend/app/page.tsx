"use client";
import api from "@/api/Api";
import authService, { AuthStatus } from "@/services/auth.service";
import Tag from "@/types/Bookmarks/Tag";
import Bookmark from "@/types/Bookmarks/Bookmark";
import BookmarkCard from "@components/bookmark/BookmarkCard";
import TagList from "@components/TagList";
import { useEffect, useState } from "react";
import useAuth from "@components/UseAuth";
import TagWithCnt from "@/types/Bookmarks/TagWithCnt";

export default function App() {
  const userAuth = useAuth();
  const [tags, setTags] = useState<TagWithCnt[]>([]);
  const [bookmarks, setBookmarks] = useState<Bookmark[]>([]);
  const [loading, setLoading] = useState(true);

  // Grab the data.
  useEffect(() => {
    if (userAuth) {
      let tagList: TagWithCnt[] = [];
      let bkmkList: Bookmark[] = [];
      Promise.all([api.getAllTags(), api.getAllBookmarks()]).then(results => {
        for (let tag of results[0].data) {
          tagList.push(tag);
        }
        setTags(tagList);

        for (let bkmk of results[1].data) {
          bkmkList.push(bkmk);
        }
        setBookmarks(bkmkList);
        setLoading(false);
     });
    }
  }, [userAuth]);

  /**
   * Ideally when the user visits the site they will actually have a cool landing page
   * rather than redirecting them immediately to sign in.
   * Meaning that the '/' will eventually be added to the public route and not authenticated will be the
   * the regular landing.
   */
  return userAuth ? (
    !loading ? (
      <div className="row">
        <div className="col-md-4 col-lg-3">
          <TagList tagsCounted={tags} />
        </div>
        <div className="col-md-8 col-lg-9">
          <div className="row no-pad">
            <div className="col-6 col-md-12 col-lg-4">
              <BookmarkCard bookmark={bookmarks[0]} />
            </div>
            <div className="col-6 col-md-12 col-lg-4">
              <BookmarkCard bookmark={bookmarks[1]} />
            </div>
            <div className="col-6 col-md-12 col-lg-4">
              <BookmarkCard bookmark={bookmarks[2]} />
            </div>
          </div>
        </div>
      </div>
    ) : (
      <p>loading</p>
    )
  ) : (
    <div> Hello Welcome to BookmarkIt. </div>
  );
}
