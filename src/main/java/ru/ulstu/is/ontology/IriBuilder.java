package ru.ulstu.is.ontology;

import java.net.URI;
import java.net.URISyntaxException;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;

import ru.ulstu.is.utils.StringUtils;

class IriBuilder {
    private final OWLOntology ontology;
    private final IRI iri;

    private IriBuilder(OWLOntology ontology) {
        this.ontology = ontology;
        this.iri = ontology.getOntologyID().getOntologyIRI().orNull();
        if (this.iri == null) {
            throw new InternalError("Ontology does not have ontologyIRI");
        }
    }

    private String format(String iri) {
        return iri.trim().replace(" ", "_");
    }

    static IriBuilder create(OWLOntology ontology) {
        return new IriBuilder(ontology);
    }

    private IRI getEntityFullIri(String entityName) {
        if (!StringUtils.hasText(entityName)) {
            throw new InternalError("Entity name can't be empty or null");
        }
        final String fragment = format(entityName);
        String base = format(iri.toString());
        if (!base.endsWith("#") && !base.endsWith("/")) {
            base += "#";
        }
        try {
            return IRI.create(new URI(base + fragment));
        } catch (URISyntaxException e) {
            throw new InternalError(e);
        }
    }

    public IRI getIri() {
        return iri;
    }

    public String getPrefix() {
        final String prefix = iri.toString();
        if (prefix.endsWith("#")) {
            return prefix;
        }
        if (prefix.endsWith("/")) {
            return prefix.substring(0, prefix.length() - 1).concat("#");
        }
        return prefix.concat("#");
    }

    public IRI createIriForNewEntity(String entityName) {
        final IRI currentIri = getEntityFullIri(entityName);
        if (ontology.containsEntityInSignature(currentIri)) {
            throw new InternalError(String.format("Entity is already exists: %s", currentIri));
        }
        return currentIri;
    }

    public IRI getIriForExistEntity(String entityName) {
        final IRI currentIri = getEntityFullIri(entityName);
        if (!ontology.containsEntityInSignature(currentIri)) {
            throw new InternalError(String.format("Entity does not exists: %s", currentIri));
        }
        return currentIri;
    }
}
