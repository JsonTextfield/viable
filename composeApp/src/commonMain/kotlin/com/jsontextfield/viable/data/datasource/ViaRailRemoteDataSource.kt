package com.jsontextfield.viable.data.datasource

import com.jsontextfield.viable.data.database.entities.Shape
import com.jsontextfield.viable.data.database.entities.Station
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order

class ViaRailRemoteDataSource(
    private val supabaseClient: SupabaseClient
) : IViaRailDataSource {
    override suspend fun getRouteShape(route: String): List<Shape> {
        val result = supabaseClient.from("trips")
            .select(Columns.raw("shapes(*)")) {
                filter {
                    eq("trip_short_name", route)
                }
                order("shape_pt_sequence", Order.ASCENDING, referencedTable = "shapes")
            }.decodeSingle<Map<String, List<Shape>>>()
        return result["shapes"] ?: emptyList()
    }

    override suspend fun getStation(code: String): Station {
        return supabaseClient.from("stops").select {
            filter {
                eq("stop_code", code)
            }
        }.decodeSingle<Station>()
    }
}