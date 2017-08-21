package hendraganteng.udacitybakingapp.network;

import java.util.List;

import hendraganteng.udacitybakingapp.network.model.Recipe;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * @author hendrawd on 8/20/17
 */

public interface BakingService {

    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> getRecipes();
}
