package com.jsontextfield.viable.data.datasource

import com.jsontextfield.viable.data.database.entities.Shape
import com.jsontextfield.viable.data.database.entities.Station
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns

class ViaRailRemoteDataSource(
    private val supabaseClient: SupabaseClient
) : IViaRailDataSource {
    override suspend fun getRouteShape(route: String): List<Shape> {
        return supabaseClient.from("trips").select {
            select(Columns.raw("shapes.shape_id"))
            filter {
                eq("route_id", route)
            }
        }.decodeList<Shape>()
    }

    override suspend fun getStation(code: String): Station {
        return supabaseClient.from("stops").select {
            filter {
                eq("stop_code", code)
            }
        }.decodeSingle<Station>()
    }
}