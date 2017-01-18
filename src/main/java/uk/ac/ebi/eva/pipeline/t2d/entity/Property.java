/*
 * Copyright 2016-2017 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.ebi.eva.pipeline.t2d.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PROP")
public class Property {

    private static final String FALSE = "FALSE";
    private static final String STRING = "STRING";
    @Id
    @Column(name = "PROP")
    private String id;

    @Column(name = "COM_DS")
    private String comDs;

    @Column(name = "COM_PH")
    private String comPh;

    @Column(name = "DB_COL")
    private String columnName;

    @Column(name = "PROP_TYPE")
    private String type;

    @Column(name = "SEARCHABLE")
    private String searchable;

    @Column(name = "DISPLAYABLE")
    private String displayable;

    @Column(name = "SORT")
    private Double sort;

    @Column(name = "MEANING")
    private String meaning;

    Property() {
    }

    public Property(String property) {
        id = property;
        comDs = FALSE;
        comPh = FALSE;
        columnName = property;
        type = STRING;
        searchable = FALSE;
        displayable = FALSE;
        meaning = property;
    }
}
