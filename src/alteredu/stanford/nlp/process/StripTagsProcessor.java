package alteredu.stanford.nlp.process;

import alteredu.stanford.nlp.ling.BasicDocument;
import alteredu.stanford.nlp.ling.Document;
import alteredu.stanford.nlp.ling.Word;

import java.util.*;

/**
 * A <code>Processor</code> whose <code>process</code> method deletes all
 * SGML/XML/HTML tags (tokens starting with <code>&lt;</code> and ending
 * with <code>&gt;<code>. Optionally, newlines can be inserted after the
 * end of block-level tags to roughly simulate where continuous text was
 * broken up (this helps finding sentence boundaries for example).
 *
 * @author Christopher Manning
 */
public class StripTagsProcessor extends AbstractListProcessor {

  /**
   * Block-level HTML tags that are rendered with surrounding line breaks.
   */
  public static final Set blockTags = new HashSet(Arrays.asList(new String[]{"blockquote", "br", "div", "h1", "h2", "h3", "h4", "h5", "h6", "hr", "li", "ol", "p", "pre", "table", "tr", "ul"}));

  /**
   * Whether to insert "\n" words after ending block tags.
   */
  private boolean markLineBreaks;

  /**
   * Constructs a new StripTagsProcessor that doesn't mark line breaks.
   */
  public StripTagsProcessor() {
    this(false);
  }

  /**
   * Constructs a new StripTagProcessor that marks line breaks as specified.
   */
  public StripTagsProcessor(boolean markLineBreaks) {
    setMarkLineBreaks(markLineBreaks);
  }

  /**
   * Retruns whether the output of the processor will contain newline words
   * ("\n") at the end of block-level tags.
   */
  public boolean getMarkLineBreaks() {
    return (markLineBreaks);
  }

  /**
   * Sets whether the output of the processor will contain newline words
   * ("\n") at the end of block-level tags.
   */
  public void setMarkLineBreaks(boolean markLineBreaks) {
    this.markLineBreaks = markLineBreaks;
  }

  /**
   * Returns a new Document with the same meta-data as <tt>in</tt>,
   * and the same words except tags are stripped.
   */
  public List process(List in) {
    List out = new ArrayList();
    boolean justInsertedNewline = false; // to prevent contiguous newlines
    for (Iterator iter = in.iterator(); iter.hasNext();) {
      Word w = (Word) iter.next();
      String ws = w.word();
      if (ws.startsWith("<") && ws.endsWith(">")) {
        if (markLineBreaks && !justInsertedNewline) {
          // finds start and end of tag name (ignores brackets and /)
          // e.g. <p>, <br/>, or </table>
          //       se   s e        s    e

          int tagStartIndex = 1;
          while (tagStartIndex < ws.length() && !Character.isLetter(ws.charAt(tagStartIndex))) {
            tagStartIndex++;
          }
          if (tagStartIndex == ws.length()) {
            continue; // no tag text
          }

          int tagEndIndex = ws.length() - 1;
          while (tagEndIndex > tagStartIndex && !Character.isLetterOrDigit(ws.charAt(tagEndIndex))) {
            tagEndIndex--;
          }

          // looks up tag name in list of known block-level tags
          String tagName = ws.substring(tagStartIndex, tagEndIndex + 1).toLowerCase();
          if (blockTags.contains(tagName)) {
            out.add(new Word("\n")); // mark newline for block-level tags
            justInsertedNewline = true;
          }
        }
      } else {
        out.add(w); // normal word
        justInsertedNewline = false;
      }
    }
    return out;
  }

  /**
   * For internal debugging purposes only.
   */
  public static void main(String[] args) {
    Document htmlDoc = new BasicDocument().init("top text <h1>HEADING text</h1> this is <p>new paragraph<br>next line<br/>xhtml break etc.");
    System.out.println("Before:");
    System.out.println(htmlDoc);
    Document txtDoc = new StripTagsProcessor(true).processDocument(htmlDoc);
    System.out.println("After:");
    System.out.println(txtDoc);
    Document sentences = new WordToSentenceProcessor().processDocument(txtDoc);
    System.out.println("Sentences:");
    System.out.println(sentences);
  }
}
