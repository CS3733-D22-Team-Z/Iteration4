package edu.wpi.cs3733.D22.teamZ.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchControl {

  private List<ISearchable> parentList;
  private List<ISearchable> filtered;
  private String matchID = "";

  // IMPORTANT: Type ID List. New types must be added here to work!
  // A note: the search will reduce any type in the search term to one letter,
  // so make sure the ID you pick is unique while also being the first letter
  // of a target word. Ex room->R, etc.
  List<String> knownIDs = new ArrayList<String>(List.of("R", "F", "T", "E", "P"));

  /**
   * This class handles map searching. It should be initialized with a list of searchable objects.
   *
   * @param parents An initial searchable list.
   */
  public SearchControl(List<ISearchable> parents) {
    parentList = new ArrayList<>(parents);
    filtered = parentList;
    matchID = constructMatchID();
  }

  /**
   * Helper method involved in the registration of type IDs. Generates a regex string to simplify
   * finding type ids.
   *
   * @return A regex string to match all type ids.
   */
  private String constructMatchID() {
    StringBuilder ret = new StringBuilder("[");
    for (String id : knownIDs) {
      ret.append(id); // this code encloses all known IDs in [], followed by a colon.
    } // this is a regex string to match exactly one of the ids.
    ret.append("]:"); // Example output: "[RFTEP]:"
    return ret.toString();
  }

  /**
   * This function is used to add more searchable to the engine post-initialization.
   *
   * @param more A list of searchables to add to the engine
   */
  public void addObjs(List<ISearchable> more) {
    parentList.addAll(more);
  }

  public List<ISearchable> getCurrentSearchable() {
    return parentList;
  }

  /** This function completely clears the searchable object list. */
  public void resetSearchableObjs() {
    parentList.clear();
  }

  /**
   * This function returns a list of searchables matched by the input search term. It is unordered.
   * This is the primary function for the class. <br>
   * <br>
   * Search term functionality: arbitrary length strings are supported without any additional
   * formatting needed. However, multi-type terms have some specific formatting. Each type should be
   * surrounded by "" and separated by a space. Type IDs should be followed by ::, though they do
   * not need to be the whole word, ex Room:: vs R:: is the same. <br>
   * <br>
   * Example searches:<br>
   * Generic search: au bon pain<br>
   * Retail type search: t::retl<br>
   * second floor retail: "t::retl" "f::2"<br>
   *
   * @param search A search term.
   * @return A list of matching searchables.
   */
  public List<ISearchable> filterList(String search) {
    /*TODO: fix? possible mem leak? doesn't work if just assignment. Not real sure.*/
    filtered = new ArrayList<>(parentList); // reset the filtered list.
    search = search.toLowerCase(); // lowercase the search term for ease of processing

    List<String> terms = divideTerm(search); // gen search term list for future use.

    List<String> termsMod = IDRemovals(terms); // Do all type id removals. Also removes
    // these terms from the list, so save new terms.
    for (String term : termsMod) { // for each term, go through the list of searchables and
      filtered
          .removeIf( // remove the entry if the term can not be found in any of its search terms.
              n ->
                  n.toSearchTerms().stream()
                      .noneMatch(str -> str.toLowerCase().matches(".*" + term + ".*")));
    } // regex explanation: matches if the term is found after any number of characters.
    // and also before any number of characters.

    return filtered;
  }

  /**
   * Helper function used by IDRemovals to simplify code. Removes nonmatched entries from the list
   * on args.
   *
   * @param ID A type identifier.
   * @param term An associated search term.
   */
  private void removeItemsByID(String ID, String term) {
    filtered.removeIf(
        n ->
            n.toSearchTerms().stream()
                .noneMatch(str -> str.toLowerCase().matches(ID + ".*" + term + ".*")));
    // largely, see above. This version also adds the ID into the regex here   ^.
  }

  /**
   * This helper function handles all the type search removals.
   *
   * @param terms An input list of search terms.
   * @return The input list with any type IDs and their associated search removed.
   */
  private List<String> IDRemovals(List<String> terms) {
    for (int i = 0; i < terms.size(); i++) { // go through all terms
      if (terms.get(i).matches(matchID)) { // if type id
        String id = terms.get(i).toLowerCase(); // lowercase it
        terms.remove(i); // remove from list
        i--; // fix i from prev line
        String term = ""; // set default search term
        if (i < (terms.size() - 1)
            && !terms.get(i + 1).matches(matchID)) { // not last and next isn't another type id
          term = terms.get(++i); // get next string as the search term
          terms.remove(i); // remove from terms list
          i--; // adjust i for prev line
        }

        if (id.equalsIgnoreCase("f:")) { // case out floor because of weird matches
          // term is 1, l1,l2,etc
          String finalTerm = term; // fixes warn/error, dunno
          filtered.removeIf(
              n ->
                  n.toSearchTerms().stream()
                      .noneMatch(str -> str.equalsIgnoreCase("f:" + finalTerm)));
          // removes if the term is not an exact match, unlike the usual regex match
          // technically doesn't work with empty type, but I can't really be bothered
        } else {
          removeItemsByID(id, term); // filter list by type id
        }
      }
    }
    return terms;
  }

  /**
   * This function divides a search term into a processable, formatted list of search terms.
   *
   * @param search An input search term.
   * @return The processed list of terms.
   */
  private List<String> divideTerm(String search) { // types get ::, multi types enclosed in ""
    // I use '' to denote non-literal quotes in comments.
    // example input search: '"room::Kessler" "floor::1"'

    // replace identifiers with their first character.
    // literally, get all words before ::, and replace them with their first character, plus an
    // additional ; identifier to help in next step
    Pattern pattern = Pattern.compile("(\\w+)(?=:{2})");
    Matcher matcher = pattern.matcher(search);
    search =
        matcher.replaceAll(
            x ->
                String.valueOf(x.group().charAt(0)).toUpperCase()
                    + ";"); // uppercase prob not necessary, oh well
    // '"R;::Kessler" "F;::1"'

    String[] terms = search.split("::| \""); // divides into terms and types. Splits on '::' or ' "'
    // '"R;', 'Kessler"', 'F;', '1"'

    List<String> terms2 = new ArrayList<>();
    for (String term : terms) {
      term = term.replace("\"", ""); // nulls any remaining quote marks
      term = term.replace(";", ":"); // ; -> :
      // 'R:', 'Kessler', 'F:', '1'
      terms2.add(term.strip()); // any extraneous ' ' removed
    }

    terms2.removeAll(
        Arrays.asList("", null)); // under some cases, empty terms may generate. remove them.
    // example: '"room::Kessler " "floor::1"' will make an empty.

    return terms2;
  }
}
