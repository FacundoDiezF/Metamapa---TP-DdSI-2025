package ar.utn.edu.frba.ddsi.converters;

import ar.utn.edu.frba.ddsi.models.entities.source.IImporter;
import ar.utn.edu.frba.ddsi.models.entities.source.impl.CsvImporter;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ImporterConverter implements AttributeConverter<IImporter, String> {
  @Override
  public String convertToDatabaseColumn(IImporter importer){

    if(importer == null) {
      return null;
    }

    if (importer instanceof CsvImporter){
      return "CSV";
    }
    return null;
  }

  @Override
  public IImporter convertToEntityAttribute(String s){
    switch (s) {
      case "CSV":
        return new CsvImporter();
      default:
        return null;
    }
  }
}
