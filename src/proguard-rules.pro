# Add any ProGuard configurations specific to this
# extension here.

-keep public class com.jaxparrow.lyricviewer.LyricViewer {
    public *;
 }
-keeppackagenames gnu.kawa**, gnu.expr**

-optimizationpasses 4
-allowaccessmodification
-mergeinterfacesaggressively

-repackageclasses 'com/jaxparrow/lyricviewer/repack'
-flattenpackagehierarchy
-dontpreverify
