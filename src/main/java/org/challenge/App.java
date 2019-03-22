package org.challenge;

import org.challenge.core.Faire;

/**
 * Hello world!
 *
 */
public class App 
{
    private static final String BRAND_DEFAULT = "b_d2481b88";

    public static void main(String[] args) {
        String brand = args.length == 2 ?args[1]: BRAND_DEFAULT;
        Faire.getInstance().execute(args[0],brand);
    }
}
