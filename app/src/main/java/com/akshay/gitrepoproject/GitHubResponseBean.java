package com.akshay.gitrepoproject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by akshaythalakoti on 2/7/18.
 */

public class GitHubResponseBean implements Serializable {


    private int total_count;
    private boolean incomplete_results;
    private List<Items> items;


    public List<Items> getItems() {
        return items;
    }


    public static class Items {

        private int id;
        private String name;
        private Owner owner;
        private String description;
        private int stargazers_count;


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }


        public Owner getOwner() {
            return owner;
        }


        public String getDescription() {
            return description;
        }


        public int getStargazers_count() {
            return stargazers_count;
        }


        public static class Owner {
            private int id;
            private String avatar_url;


            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getAvatar_url() {
                return avatar_url;
            }


        }
    }
}
