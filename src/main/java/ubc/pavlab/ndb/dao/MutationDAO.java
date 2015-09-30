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

package ubc.pavlab.ndb.dao;

import java.util.List;

import ubc.pavlab.ndb.exceptions.DAOException;
import ubc.pavlab.ndb.model.dto.MutationDTO;

/**
 * TODO Document Me
 * 
 * @author mjacobson
 * @version $Id$
 */
public interface MutationDAO {
    // Actions ------------------------------------------------------------------------------------

    /**
     * Returns the mutation from the database matching the given ID, otherwise null.
     * 
     * @param id The ID of the mutation to be returned.
     * @return The mutation from the database matching the given ID, otherwise null.
     * @throws DAOException If something fails at database level.
     */
    public MutationDTO find( Integer id ) throws DAOException;

    /**
     * Returns a list of mutations from the database matching the given gene ID.
     * 
     * @param geneId The gene ID of the mutations to be returned.
     * @return A list of mutations from the database matching the given gene ID.
     * @throws DAOException If something fails at database level.
     */
    public List<MutationDTO> findByGeneId( Integer geneId ) throws DAOException;

    /**
     * Returns a list of mutations from the database matching the given paper ID.
     * 
     * @param paperId The paper ID of the mutations to be returned.
     * @return A list of mutations from the database matching the given paper ID.
     * @throws DAOException If something fails at database level.
     */
    public List<MutationDTO> findByPaperId( Integer paperId ) throws DAOException;

    /**
     * Returns a list of all mutations from the database ordered by ID. The list is never null and
     * is empty when the database does not contain any mutations.
     * 
     * @return A list of all mutations from the database ordered by ID.
     * @throws DAOException If something fails at database level.
     */
    public List<MutationDTO> list() throws DAOException;

}
