package restR.crud;

import java.util.List;

import java.util.Optional;

import org.locationtech.jts.geom.Point;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import restR.entities.ObjectEntity;

@Primary
public interface ObjectCrud extends JpaRepository<ObjectEntity, String> {
	public Optional<ObjectEntity> findByObjectIdAndActiveTrue(@Param("ObjectId") String objectId);

	public List<ObjectEntity> findAllByActiveTrue(Pageable pageable);

	public List<ObjectEntity> findAllByType(@Param("Type") String type, Pageable pageable);

	public List<ObjectEntity> findAllByTypeAndActiveTrue(@Param("Type") String type, Pageable pageable);
	

	public List<ObjectEntity> findAllByAlias(@Param("Alias") String alias, Pageable pageable);

	public List<ObjectEntity> findAllByAliasContaining(@Param("Pattern") String pattern, Pageable pageable);
	
	
	public List<ObjectEntity> findAllByTypeAndAliasAndActiveTrue(@Param("Type") String type,@Param("Alias") String alias, Pageable pageable);
	
	public List<ObjectEntity> findAllByAliasAndActiveTrue(@Param("Alias") String alias, Pageable pageable);

	public List<ObjectEntity> findAllByAliasContainingAndActiveTrue(@Param("Pattern") String pattern,
			Pageable pageable);

	/**
	 * This method finds all objects within a specified distance of a given point.
	 *
	 * @param distanceUnits The unit of measurement for the distance (e.g., "MILES",
	 *                      "KILOMETERS").
	 * @param geom          The geographic point to search from.
	 * @param distance      The distance to search in the chosen units.
	 * @param pageable      Information about the paging and sorting of the results.
	 * @return A list of {@link ObjectEntity} objects found within the specified
	 *         distance.
	 */
	@Query(value = "SELECT o" + " FROM ObjectEntity o"
			+ " WHERE CAST(st_dwithin(o.geom, :geom ,:distance * (CASE WHEN :distanceUnits='MILES' THEN 1609.344 ELSE 1000.0 END)"
			+ ", true) AS boolean)")

	/*
	 * Uses the `st_dwithin` spatial function to check if two geometries are within
	 * the specified distance. This function considers the spatial reference system
	 * (SRID) of the geometries. For geographic geometries (representing locations
	 * on Earth), distances are measured in meters by default (assuming a spherical
	 * Earth). Set `use_spheroid=false` for faster but less accurate measurement on
	 * a flat surface.M * 
	 * **Distance Unit Conversion:** 
	 *  - Convert meters to kilometers: `distanceInKm = distanceInMeters X 1000
	 *  - Convert meters to miles: `distanceInMiles = distanceInMeters X 1609.344
	 */
	public List<ObjectEntity> findAllByDistance(@Param("distanceUnits") String distanceUnits,
			@Param("geom") Point point, @Param("distance") double distance, Pageable pageable);

	/**
	 * This method finds all objects within a specified distance of a given point
	 * and are active.
	 *
	 * @param distanceUnits The unit of measurement for the distance (e.g., "MILES",
	 *                      "KILOMETERS").
	 * @param geom          The geographic point to search from.
	 * @param distance      The distance to search in the chosen units.
	 * @param pageable      Information about the paging and sorting of the results.
	 * @return A list of {@link ObjectEntity} objects found within the specified
	 *         distance.
	 */

	@Query(value = "SELECT o" + " FROM ObjectEntity o"
			+ " WHERE CAST(st_dwithin(o.geom, :geom ,:distance * (CASE WHEN :distanceUnits='MILES' THEN 1609.344 ELSE 1000.0 END)"
			+ ", true) AS boolean)=true AND o.active=true")
	
	/*
	 * Uses the `st_dwithin` spatial function to check if two geometries are within
	 * the specified distance. This function considers the spatial reference system
	 * (SRID) of the geometries. For geographic geometries (representing locations
	 * on Earth), distances are measured in meters by default (assuming a spherical
	 * Earth). Set `use_spheroid=false` for faster but less accurate measurement on
	 * a flat surface.M * 
	 * **Distance Unit Conversion:** :
	 *  - Convert meters to kilometers: `distanceInKm = distanceInMeters X 1000
	 *  - Convert meters to miles: `distanceInMiles = distanceInMeters X 1609.344
	 */
	public List<ObjectEntity> findAllByDistanceAndActiveTrue(@Param("distanceUnits") String distanceUnits,
			@Param("geom") Point point, @Param("distance") double distance, Pageable pageable);
	
	
	/**
	 * This method finds all objects within a specified distance of a given point.
	 *
	 * @param distanceUnits The unit of measurement for the distance (e.g., "MILES",
	 *                      "KILOMETERS").
	 * @param geom          The geographic point to search from.
	 * @param distance      The distance to search in the chosen units.
	 * @param pageable      Information about the paging and sorting of the results.
	 * @return A list of {@link ObjectEntity} objects found within the specified
	 *         distance.
	 */
	@Query(value = "SELECT o" + " FROM ObjectEntity o"
			+ " WHERE CAST(st_dwithin(o.geom, :geom ,:distance) AS boolean)")

	/*
	 * Uses the `st_dwithin` spatial function to check if two geometries are within
	 * the specified distance. This function considers the spatial reference system
	 * (SRID) of the geometries. For geographic geometries (representing locations
	 * on Earth), distances are measured in meters by default (assuming a spherical
	 * Earth). Set `use_spheroid=false` for faster but less accurate measurement on
	 * a flat surface.M * 
	 * **Distance Unit Conversion:** 
	 *  - Convert meters to kilometers: `distanceInKm = distanceInMeters X 1000
	 *  - Convert meters to miles: `distanceInMiles = distanceInMeters X 1609.344
	 */
	public List<ObjectEntity> findAllByDistance(
			@Param("geom") Point point, @Param("distance") double distance, Pageable pageable);

	/**
	 * This method finds all objects within a specified distance of a given point
	 * and are active.
	 *
	 * @param distanceUnits The unit of measurement for the distance (e.g., "MILES",
	 *                      "KILOMETERS").
	 * @param geom          The geographic point to search from.
	 * @param distance      The distance to search in the chosen units.
	 * @param pageable      Information about the paging and sorting of the results.
	 * @return A list of {@link ObjectEntity} objects found within the specified
	 *         distance.
	 */

	@Query(value = "SELECT o" + " FROM ObjectEntity o"
			+ " WHERE CAST(st_dwithin(o.geom, :geom ,:distance) AS boolean)=true AND o.active=true")
	
	/*
	 * Uses the `st_dwithin` spatial function to check if two geometries are within
	 * the specified distance. This function considers the spatial reference system
	 * (SRID) of the geometries. For geographic geometries (representing locations
	 * on Earth), distances are measured in meters by default (assuming a spherical
	 * Earth). Set `use_spheroid=false` for faster but less accurate measurement on
	 * a flat surface.M * 
	 * **Distance Unit Conversion:** :
	 *  - Convert meters to kilometers: `distanceInKm = distanceInMeters X 1000
	 *  - Convert meters to miles: `distanceInMiles = distanceInMeters X 1609.344
	 */
	public List<ObjectEntity> findAllByDistanceAndActiveTrue(
			@Param("geom") Point point, @Param("distance") double distance, Pageable pageable);
	
	
	/* @Query(value = "SELECT o FROM ObjectEntity o" +
	            " WHERE o.type = 'Table' AND o.active = true AND  "
	            + "json_extract_path(o.objectDetails,'capacity') = :capacity ")
	 public List<ObjectEntity> findAllTablesByCapacity(@Param("capacity") int capacity, Pageable pageable);*/
}
