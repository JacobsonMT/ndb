/*
 * The ndb project
 * 
 * Copyright (c) 2015 University of British Columbia
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package ubc.pavlab.ndb.model.dto;

import ubc.pavlab.ndb.model.Gene;

/**
 * Data Transfer Object for {@link Gene}
 * 
 * @author mbelmadani
 * @version $Id$
 */
public final class PaperDTO {
    private final Integer id;
    private final String url;
    private final String author;
    private final String paper_table;
    private final String mut_reporting;
    private final String scope;
    private final boolean parents;
    private final String cohort;
    private final String cohort_source;
    private final Integer cohort_size;
    private final String reported_effects;

    public PaperDTO( Integer id, String url, String author, String paper_table, String mut_reporting, String scope,
            boolean parents, String cohort, String cohort_source, Integer cohort_size, String reported_effects ) {
        super();
        this.id = id;
        this.url = url;
        this.author = author;
        this.paper_table = paper_table;
        this.mut_reporting = mut_reporting;
        this.scope = scope;
        this.parents = parents;
        this.cohort = cohort;
        this.cohort_source = cohort_source;
        this.cohort_size = cohort_size;
        this.reported_effects = reported_effects;
    }

    public Integer getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getAuthor() {
        return author;
    }

    public String getPaper_table() {
        return paper_table;
    }

    public String getMut_reporting() {
        return mut_reporting;
    }

    public String getScope() {
        return scope;
    }

    public boolean isParents() {
        return parents;
    }

    public String getCohort() {
        return cohort;
    }

    public String getCohort_source() {
        return cohort_source;
    }

    public Integer getCohort_size() {
        return cohort_size;
    }

    public String getReported_effects() {
        return reported_effects;
    }

}
