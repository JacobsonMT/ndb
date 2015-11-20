package ubc.pavlab.ndb.beans;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import ubc.pavlab.ndb.beans.services.CacheService;
import ubc.pavlab.ndb.beans.services.VariantService;
import ubc.pavlab.ndb.model.Event;
import ubc.pavlab.ndb.model.EventListFactory;
import ubc.pavlab.ndb.model.Variant;

@ManagedBean
@ViewScoped
public class ResultsView implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -506601459390058684L;

    private static final Logger log = Logger.getLogger( ResultsView.class );

    private String query;

    @ManagedProperty("#{variantService}")
    private VariantService variantService;

    @ManagedProperty("#{cacheService}")
    private CacheService cacheService;

    List<Variant> mutations;
    List<Event> events;

    Variant selectedVariant;

    public Variant getSelectedVariant() {
        return selectedVariant;
    }

    public void setSelectedVariant( Variant trunk ) {
        this.selectedVariant = trunk;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents( List<Event> events ) {
        this.events = events;
    }

    public List<Variant> getMutations() {
        return mutations;
    }

    public void setMutations( List<Variant> mutations ) {
        this.mutations = mutations;
    }

    @PostConstruct
    public void init() {
        if ( FacesContext.getCurrentInstance().getPartialViewContext().isAjaxRequest() ) {
            return; // Skip ajax requests.
        }
        log.info( "init" );
        Map<String, String> requestParams = FacesContext.getCurrentInstance().getExternalContext()
                .getRequestParameterMap();

        String ncbiGeneId = requestParams.get( "NCBIGeneId" );
        String chr = requestParams.get( "chr" );
        String start = requestParams.get( "start" );
        String stop = requestParams.get( "stop" );
        if ( !StringUtils.isBlank( ncbiGeneId ) ) {
            // Search by Genee2
            try {
                Integer geneId = Integer.parseInt( ncbiGeneId );
                this.query = cacheService.getGeneById( geneId ).getSymbol();
                this.mutations = this.variantService.fetchByGeneId( geneId );
            } catch ( NumberFormatException | NullPointerException e ) {
                throw new IllegalArgumentException( "Malformed Search Parameters" );
            }

        } else if ( !StringUtils.isBlank( chr ) && !StringUtils.isBlank( start ) && !StringUtils.isBlank( stop ) ) {
            // Search by coordinates
            try {
                Integer startCoord = Integer.parseInt( start );
                Integer stopCoord = Integer.parseInt( stop );
                this.query = chr + ":" + start + "-" + stop;
                this.mutations = this.variantService.fetchByPosition( chr, startCoord, stopCoord );
            } catch ( NumberFormatException e ) {
                throw new IllegalArgumentException( "Malformed Search Parameters" );
            }
        } else {
            // Unknown Search
            throw new IllegalArgumentException( "Unknown Search Parameters" );
        }

        EventListFactory eventListFactory = new EventListFactory( mutations );
        events = eventListFactory.getEventList();

    }

    public String getQuery() {
        return query;
    }

    public void setQuery( String query ) {
        this.query = query;
    }

    public void setVariantService( VariantService variantService ) {
        this.variantService = variantService;
    }

    public void setCacheService( CacheService cacheService ) {
        this.cacheService = cacheService;
    }

}