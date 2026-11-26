/**
 * 11/24/2026 Class created.
 *
 */

package com.ars.vmt.repositories;


public class RepoUtility {

    /**
     * Build a String of comma-separated '?'
     * @param length int
     * @return String
     */
    public static String buildParamsPlaceholders(int length) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            if (i == length - 1) {
                sb.append("?");
            } else {
                sb.append("?, ");
            }
        }

        return sb.toString();
    }

}
