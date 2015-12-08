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

package ubc.pavlab.ndb.beans.services;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import org.apache.log4j.Logger;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;

import ubc.pavlab.ndb.model.Gene;

/**
 * Service layer meant to be an entry point to retrieve all aggregate database statistics. Will implement caches.
 * 
 * @author mjacobson
 * @version $Id$
 */
@ManagedBean
@ApplicationScoped
public class StatsService implements Serializable {

    private static final long serialVersionUID = -2618479583725470865L;

    private static final Logger log = Logger.getLogger( StatsService.class );

    private static final int EXPIRATION_TIME = 1;

    private static final TimeUnit EXPIRATION_TIME_UNIT = TimeUnit.HOURS;

    private static final Integer TOP_N = 5;

    @ManagedProperty("#{variantService}")
    private VariantService variantService;

    private final Supplier<Integer> latestPaperCnt = Suppliers.memoizeWithExpiration( paperCntSupplier(),
            EXPIRATION_TIME,
            EXPIRATION_TIME_UNIT );
    private final Supplier<Integer> latestVariantCnt = Suppliers.memoizeWithExpiration( variantCntSupplier(),
            EXPIRATION_TIME,
            EXPIRATION_TIME_UNIT );
    private final Supplier<Integer> latestEventCnt = Suppliers.memoizeWithExpiration( eventCntSupplier(),
            EXPIRATION_TIME,
            EXPIRATION_TIME_UNIT );
    private final Supplier<Integer> latestSubjectCnt = Suppliers.memoizeWithExpiration( subjectCntSupplier(),
            EXPIRATION_TIME,
            EXPIRATION_TIME_UNIT );

    private final Supplier<List<Gene>> latestTopGenesByVariantCnt = Suppliers.memoizeWithExpiration(
            topGenesByVariantCntSupplier(),
            EXPIRATION_TIME,
            EXPIRATION_TIME_UNIT );

    private final Supplier<List<Gene>> latestTopGenesByEventCnt = Suppliers.memoizeWithExpiration(
            topGenesByEventCntSupplier(),
            EXPIRATION_TIME,
            EXPIRATION_TIME_UNIT );

    public StatsService() {
        log.info( "StatsService created" );
    }

    @PostConstruct
    public void init() {
        log.info( "StatsService init" );

    }

    public int getPaperCnt() {
        return latestPaperCnt.get();
    }

    public int getVariantCnt() {
        return latestVariantCnt.get();
    }

    public int getEventCnt() {
        return latestEventCnt.get();
    }

    public int getSubjectCnt() {
        return latestSubjectCnt.get();
    }

    public List<Gene> getTopGenesByVariantCnt() {
        return latestTopGenesByVariantCnt.get();
    }

    public List<Gene> getTopGenesByEventCnt() {
        return latestTopGenesByEventCnt.get();
    }

    private Supplier<List<Gene>> topGenesByVariantCntSupplier() {
        return new Supplier<List<Gene>>() {
            @Override
            public List<Gene> get() {
                log.info( "topGenesByVariantCntSupplier" );
                return variantService.fetchTopGenesByVariantCnt( TOP_N );
            }
        };
    }

    private Supplier<List<Gene>> topGenesByEventCntSupplier() {
        return new Supplier<List<Gene>>() {
            @Override
            public List<Gene> get() {
                log.info( "topGenesByEventCntSupplier" );
                return variantService.fetchTopGenesByEventCnt( TOP_N );
            }
        };
    }

    private Supplier<Integer> paperCntSupplier() {
        return new Supplier<Integer>() {
            @Override
            public Integer get() {
                log.info( "paperCntSupplier" );
                return variantService.fetchPaperCntWithVariants();
            }
        };
    }

    private Supplier<Integer> variantCntSupplier() {
        return new Supplier<Integer>() {
            @Override
            public Integer get() {
                log.info( "variantCntSupplier" );
                return variantService.fetchVariantCnt();
            }
        };
    }

    private Supplier<Integer> eventCntSupplier() {
        return new Supplier<Integer>() {
            @Override
            public Integer get() {
                log.info( "eventCntSupplier" );
                return variantService.fetchEventCnt();
            }
        };
    }

    private Supplier<Integer> subjectCntSupplier() {
        return new Supplier<Integer>() {
            @Override
            public Integer get() {
                log.info( "subjectCntSupplier" );
                return variantService.fetchSubjectCnt();
            }
        };
    }

    public void setVariantService( VariantService variantService ) {
        this.variantService = variantService;
    }

}