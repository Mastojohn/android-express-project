package com.app.express.task;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.app.express.activity.NextDelivery;
import com.app.express.db.persistence.Delivery;
import com.app.express.db.persistence.Round;
import com.app.express.db.persistence.User;
import com.app.express.helper.App;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class RoundTask extends AsyncTask<Boolean, Integer, Integer> {

	private static final String TOAST_MSG = "Calcul de l'itinéraire en cours...";
	private static final String TOAST_ERR_MAJ = "Impossible de trouver un itinéraire pour ce parcours !";
	private static final String TOAST_ERR_OPEN_STREAM = "Impossible d'ouvrir une connexion au service web Google Direction. Vérifiez vos paramètres réseaux.";
	private static final String TOAST_ERR_GOOGLE = "Une erreur est survenue lors de l'appel au service \"Google Destination\" pour déterminer le parcours.";
	private static final String TOAST_WS_OK = "Le parcours a été calculé, mise à jour de la carte en cours...";
	private static final int MAX_POINTS = 8;

	private GoogleMap map;
	private Location location;
	private String startStep;
	private String endStep;

	/**
	 * Markers displayed now.
	 */
	public static ArrayList<Marker> markersDisplayed = new ArrayList<Marker>();

	/**
	 * Polyline displayed now.
	 */
	public static Polyline polylineDisplayed;

	/**
	 * Deliveries where the deliverer must deliver to sort.
	 */
	private List<Delivery> listDeliveriesToSort;

	/**
	 * Deliveries where the deliverer must deliver to sort.
	 */
	public static List<HashMap<String, Object>> listDeliveriesSorted = new ArrayList<HashMap<String, Object>>();

	public static LatLng southwest;
	public static LatLng northeast;

	/**
	 * The list of LatLng points who are in the best sort for the deliverer.
	 */
	private static List<LatLng> listLatLngSorted = new ArrayList<LatLng>();
	
	/**
	 * The round can be partially loaded. Used for be sure it correctly loaded.
	 */
	public static boolean roundCorrectlyLoaded = false;

	/**
	 * Constructor.
	 * 
	 * @param map
	 * @param locationSource
	 * @param listDeliveriesToSort
	 */
	public RoundTask(final GoogleMap map, final Location location, List<Delivery> listDeliveriesToSort) {
		SharedPreferences settings = App.context.getSharedPreferences(NextDelivery.PREFS_NAME, App.context.MODE_PRIVATE);

		this.map = map;
		this.location = location;
		Log.i("Gmap", location.toString());
		// Get the current location.
		startStep = location.getLatitude() + "," + location.getLongitude();

		// Get the location of the enterprise.
		endStep = settings.getString("defaultStartRound", location.getLatitude() + "," + location.getLongitude());// TODO Faire un système de settings
																													// permettant de définir l'emplacement de
		// l'entreprise.

		this.listDeliveriesToSort = listDeliveriesToSort;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onPreExecute() {
		Toast.makeText(App.context, TOAST_MSG, Toast.LENGTH_LONG).show();
	}

	/**
	 * [0]: Force: If false and if the round was already calculated, use the last values for refresh the view. [false]
	 * {@inheritDoc}
	 */
	@Override
	protected Integer doInBackground(Boolean... params) {
		try {
			if((listDeliveriesSorted.size() > 0 && listLatLngSorted.size() > 0) && (params == null || params.length == 0 || params[0] == false)){
				// Don't force because no params[0] or false. Return 1 for refresh the view with the last datas. (Static data are already calculated and available !)
				Log.i("RoundTask", "RoundTask.execute() => Utilisation du cache !");
				return 1;
			}else{
				Log.i("RoundTask", "RoundTask.execute() => Appel du web service en cours de préparation !");
				
				// Reset the sorted lists.
				listDeliveriesSorted = new ArrayList<HashMap<String, Object>>();
				listLatLngSorted = new ArrayList<LatLng>();
			}
			
			String waypoints = "&waypoints=";
			// MAX_POINTS Points maximum. Free mode.
			for (int i = 0; i < (listDeliveriesToSort.size() >= MAX_POINTS ? MAX_POINTS : listDeliveriesToSort.size()); i++) {
				Delivery delivery = (Delivery) listDeliveriesToSort.get(i);
				User receiver = delivery.getReceiver();
				waypoints += receiver.getAddress() + (receiver.getZipCode().length() > 0 ? "," + receiver.getZipCode() : "") + "," + (receiver.getCity().length() > 0 ? receiver.getCity() + "|" : "");
			}

			SharedPreferences settings = App.context.getSharedPreferences(NextDelivery.PREFS_NAME, App.context.MODE_PRIVATE);

			// Build the web service URL to call.
			final StringBuilder url = new StringBuilder("http://maps.googleapis.com/maps/api/directions/xml?sensor=false&language=fr");
			url.append("&origin=");
			url.append(startStep.replace(' ', '+'));
			url.append("&destination=");
			url.append(endStep.replace(' ', '+'));
			url.append(waypoints.replaceAll("\\s", "+"));
			url.append("&avoidTolls=" + settings.getBoolean("avoidTolls", false));// Avoid main roads.
			url.append("&optimizeWaypoints=" + settings.getBoolean("optimizeWaypoints", true));// Optimize round.

			Log.i("RoundTask", "Url du webservice google appelée: \n" + url.toString());

			InputStream stream;
			try {
				// Appel du web service
				stream = new URL(url.toString()).openStream();
			} catch (Exception e) {
				Log.w("RoundTask", "Erreur lors de l'appel au web service: ", e);
				return -2;
			}

			// Traitement des données
			final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			documentBuilderFactory.setIgnoringComments(true);

			final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

			final Document document = documentBuilder.parse(stream);
			document.getDocumentElement().normalize();

			// On récupère d'abord le status de la requête
			final String status = document.getElementsByTagName("status").item(0).getTextContent();

			if (!"OK".equals(status)) {
				Log.w("RoundTask", "Erreur lors de l'appel au web service: " + status);
				return -1;
			}

			southwest = new LatLng(Double.parseDouble(document.getElementsByTagName("southwest").item(0).getChildNodes().item(1).getTextContent()), Double.parseDouble(document.getElementsByTagName("southwest").item(0).getChildNodes().item(3).getTextContent()));
			northeast = new LatLng(Double.parseDouble(document.getElementsByTagName("northeast").item(0).getChildNodes().item(1).getTextContent()), Double.parseDouble(document.getElementsByTagName("northeast").item(0).getChildNodes().item(3).getTextContent()));

			// Get the number of leg.
			int countLeg = document.getElementsByTagName("leg").getLength();

			// For each leg.
			for (int j = 0; j < countLeg; j++) {
				// On récupère les steps
				final Element elementLeg = (Element) document.getElementsByTagName("leg").item(j);
				final NodeList nodeListStep = elementLeg.getElementsByTagName("step");
				final int length = nodeListStep.getLength();

				for (int i = 0; i < length; i++) {
					final Node nodeStep = nodeListStep.item(i);

					if (nodeStep.getNodeType() == Node.ELEMENT_NODE) {
						final Element elementStep = (Element) nodeStep;

						// On décode les points du XML
						decodePolylines(elementStep.getElementsByTagName("points").item(0).getTextContent());
					}
				}

				// Object who will contains a entire leg.
				HashMap<String, Object> leg = new HashMap<String, Object>();

				// For all key who for which we want the values.
				// // Object who will contains only the values. Like duration=>value and duration=>text.
				// HashMap<String, Object> group = new HashMap<String, Object>();
				//
				// // Get the node who had the name of the field defined before. Like duration.
				// NodeList nodeList = elementLeg.getElementsByTagName(elementsGroup.get(i));
				// int nodeLength = nodeList.getLength();
				//
				// for(int k = 0; k < nodeLength; k++){
				// Node tempNode = nodeListStep.item(k);
				//
				// if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
				// NamedNodeMap nodeMap = tempNode.getAttributes();
				//
				// for (int l = 0; l < nodeMap.getLength(); l++) {
				// Node node = nodeMap.item(l);
				// group.put(node.getNodeName(), node.getNodeValue());
				// }
				// }
				// }
				// // Add the group of elements to the leg.
				// leg.put(elementsGroup.get(i), group);

				// Get and add values for start_location.
				// HashMap<String, Object> start_location = new HashMap<String, Object>();
				//
				// NodeList start_locationList = elementLeg.getChildNodes();
				// int start_locationListLength = start_locationList.getLength();
				// NamedNodeMap start_locationMap = start_locationList.item(start_locationListLength - 4).getAttributes();
				// start_location.put(start_locationMap.item(0).getNodeName(), start_locationMap.item(0).getNodeValue());
				// start_location.put(start_locationMap.item(1).getNodeName(), start_locationMap.item(1).getNodeValue());
				// leg.put("start_location", start_location);
				//
				// // Get and add values for start_location.
				// HashMap<String, Object> end_location = new HashMap<String, Object>();
				// NodeList end_locationList = elementLeg.getElementsByTagName("end_location");
				// int end_locationListLength = end_locationList.getLength();
				// NamedNodeMap end_locationMap = end_locationList.item(end_locationListLength - 1).getAttributes();
				// start_location.put(end_locationMap.item(0).getNodeName(), end_locationMap.item(0).getNodeValue());
				// start_location.put(end_locationMap.item(1).getNodeName(), end_locationMap.item(1).getNodeValue());
				// leg.put("end_location", end_location);

				leg.put("start_address", elementLeg.getElementsByTagName("start_address").item(0).getTextContent());
				leg.put("end_address", elementLeg.getElementsByTagName("end_address").item(0).getTextContent());

				// Put the values into the static object.
				listDeliveriesSorted.add(leg);
			}

			return 1;
		} catch (final Exception e) {
			Log.w("RoundTask", TOAST_ERR_MAJ, e);
			return 0;
		}
	}

	/**
	 * Decode points encoded to lat/lng.
	 * 
	 * @param encodedPoints
	 */
	private void decodePolylines(final String encodedPoints) {
		int index = 0;
		int lat = 0, lng = 0;

		while (index < encodedPoints.length()) {
			int b, shift = 0, result = 0;

			do {
				b = encodedPoints.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);

			int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lat += dlat;
			shift = 0;
			result = 0;

			do {
				b = encodedPoints.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);

			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;

			listLatLngSorted.add(new LatLng((double) lat / 1E5, (double) lng / 1E5));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onPostExecute(final Integer result) {
		if(result == -2){
			Toast.makeText(App.context, TOAST_ERR_OPEN_STREAM, Toast.LENGTH_LONG).show();
		}else if (result == -1) {
			Toast.makeText(App.context, TOAST_ERR_GOOGLE, Toast.LENGTH_LONG).show();
		} else if (result == 0) {
			Toast.makeText(App.context, TOAST_ERR_MAJ, Toast.LENGTH_LONG).show();
		} else if (result == 1) {
			Toast.makeText(App.context, TOAST_WS_OK, Toast.LENGTH_LONG).show();

			// On déclare le polyline, c'est-à-dire le trait (ici bleu) que l'on ajoute sur la carte pour tracer l'itinéraire
			final PolylineOptions polylines = new PolylineOptions();
			polylines.color(Color.BLUE);

			// On construit le polyline
			for (final LatLng latLng : listLatLngSorted) {
				polylines.add(latLng);
			}

			SharedPreferences settings = App.context.getSharedPreferences(NextDelivery.PREFS_NAME, App.context.MODE_PRIVATE);
			int listSize = listDeliveriesSorted.size();

			// Remove old markers if exists.
			if (markersDisplayed.size() > 0) {
				for (int i = 0; i < markersDisplayed.size(); i++) {
					markersDisplayed.get(i).remove();
				}
				// Remove the polyline.
				if (polylineDisplayed != null) {
					polylineDisplayed.remove();
				}
			}

			// Temp var who contains markersOptions.
			ArrayList<MarkerOptions> markersOptions = new ArrayList<MarkerOptions>();

			// Refresh the array of markers.
			for (int i = 0; i < listSize; i++) {
				MarkerOptions markerOptions = new MarkerOptions();
				markerOptions.position(Round.getLatLngByAddress(listDeliveriesSorted.get(i).get("start_address").toString()));
				markerOptions.title((i == 0 ? "Départ" : (i == listSize - 1 ? "Dernière livraison" : "Livraison N°" + i)));
				markerOptions.snippet(listDeliveriesSorted.get(i).get("start_address").toString().replaceAll(",\\s", ",\n"));

				// Custom markerOptions.
				if (i == 0) {
					// Start point.
					markerOptions.icon(BitmapDescriptorFactory.defaultMarker(settings.getFloat("firstMarkerColor", 120.0f)));
				} else if (i == listSize - 1) {
					// Last point.
					markerOptions.icon(BitmapDescriptorFactory.defaultMarker(settings.getFloat("lastMarkerColor", 0.0f)));
				} else {
					// Other points.
					markerOptions.icon(BitmapDescriptorFactory.defaultMarker(settings.getFloat("defaultMarkerColor", 270.0f)));
				}

				// Add the markerOption to the array.
				markersOptions.add(markerOptions);
			}

			// Update the map.
			// map.moveCamera(CameraUpdateFactory.newLatLngZoom(listLatLngSorted.get(0), 13));
			map.moveCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(southwest, northeast), 50));

			// Display news markers.
			for (int i = 0; i < listSize; i++) {
				MarkerOptions markerOptions = new MarkerOptions();
				// Get the marker from the static array.
				markerOptions = markersOptions.get(i);

				try {
					Marker marker = map.addMarker(markerOptions);

					// Put the marker into the static array.
					markersDisplayed.add(marker);
				} catch (IllegalArgumentException e) {
					Toast.makeText(App.context, "Une erreur réseau est survenue et a empêché la conversion des adresses de livraison vers les points géographiques. (Lat/lng)", Toast.LENGTH_LONG).show();
				}
			}
			// Add the line.
			map.addPolyline(polylines);
			
			// Signal that the round is available.
			roundCorrectlyLoaded = true;
		}
	}
}
