package com.app.express.task;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.app.express.helper.App;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class RoundTask extends AsyncTask<Void, Integer, Boolean> {

	private static final String TOAST_MSG = "Calcul de l'itin�raire en cours...";
	private static final String TOAST_ERR_MAJ = "Impossible de trouver un itin�raire pour ce parcours !";

	private GoogleMap map;
	private Location location;
	private String startStep;
	private String endStep;
	private final ArrayList<LatLng> listLatLng = new ArrayList<LatLng>();

	/**
	 * Constructor.
	 * 
	 * @param map
	 * @param locationSource
	 */
	public RoundTask(final GoogleMap map, final Location location) {
		this.map = map;
		this.location = location;
		Log.i("Gmap", location.toString());
		// Get the current location.
		startStep = location.getLatitude()+","+location.getLongitude();
		
		// Get the location of the enterprise.
		endStep = "Marseille";// TODO Faire un syst�me de settings permettant de d�finir l'emplacement de l'entreprise.
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onPreExecute() {
		Toast.makeText(App.context, TOAST_MSG, Toast.LENGTH_LONG).show();
	}

	/***
	 * {@inheritDoc}
	 */
	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			// Construction de l'url � appeler
			final StringBuilder url = new StringBuilder("http://maps.googleapis.com/maps/api/directions/xml?sensor=false&language=fr");
			url.append("&origin=");
			url.append(startStep.replace(' ', '+'));
			url.append("&destination=");
			url.append(endStep.replace(' ', '+'));

			// Appel du web service
			final InputStream stream = new URL(url.toString()).openStream();

			// Traitement des donn�es
			final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			documentBuilderFactory.setIgnoringComments(true);

			final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

			final Document document = documentBuilder.parse(stream);
			document.getDocumentElement().normalize();

			// On r�cup�re d'abord le status de la requ�te
			final String status = document.getElementsByTagName("status").item(0).getTextContent();
			if (!"OK".equals(status)) {
				return false;
			}

			// On r�cup�re les steps
			final Element elementLeg = (Element) document.getElementsByTagName("leg").item(0);
			final NodeList nodeListStep = elementLeg.getElementsByTagName("step");
			final int length = nodeListStep.getLength();

			for (int i = 0; i < length; i++) {
				final Node nodeStep = nodeListStep.item(i);

				if (nodeStep.getNodeType() == Node.ELEMENT_NODE) {
					final Element elementStep = (Element) nodeStep;

					// On d�code les points du XML
					decodePolylines(elementStep.getElementsByTagName("points").item(0).getTextContent());
				}
			}

			return true;
		} catch (final Exception e) {
			return false;
		}
	}

	/**
	 * M�thode qui d�code les points en latitudes et longitudes
	 * 
	 * @param points
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

			listLatLng.add(new LatLng((double) lat / 1E5, (double) lng / 1E5));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onPostExecute(final Boolean result) {
		if (!result) {
			Toast.makeText(App.context, TOAST_ERR_MAJ, Toast.LENGTH_SHORT).show();
		} else {
			// On d�clare le polyline, c'est-�-dire le trait (ici bleu) que l'on ajoute sur la carte pour tracer l'itin�raire
			final PolylineOptions polylines = new PolylineOptions();
			polylines.color(Color.BLUE);

			// On construit le polyline
			for (final LatLng latLng : listLatLng) {
				polylines.add(latLng);
			}

			// On d�clare un marker vert que l'on placera sur le d�part
			final MarkerOptions markerA = new MarkerOptions();
			markerA.position(listLatLng.get(0));
			markerA.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

			// On d�clare un marker rouge que l'on mettra sur l'arriv�e
			final MarkerOptions markerB = new MarkerOptions();
			markerB.position(listLatLng.get(listLatLng.size() - 1));
			markerB.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

			// On met � jour la carte
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(listLatLng.get(0), 10));
			map.addMarker(markerA);
			map.addPolyline(polylines);
			map.addMarker(markerB);
		}
	}
}
