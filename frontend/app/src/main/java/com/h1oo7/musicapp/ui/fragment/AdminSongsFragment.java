package com.h1oo7.musicapp.ui.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.h1oo7.musicapp.R;
import com.h1oo7.musicapp.model.Song;
import com.h1oo7.musicapp.model.response.SongResponse;
import com.h1oo7.musicapp.network.ApiService;
import com.h1oo7.musicapp.network.RetrofitClient;
import com.h1oo7.musicapp.ui.adapter.AdminSongAdapter;

import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminSongsFragment extends Fragment {

    private RecyclerView recyclerSongs;
    private AdminSongAdapter adapter;

    private Uri songUri, imageUri;

    private final ActivityResultLauncher<Intent> songPicker = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                    songUri = result.getData().getData();
                    Toast.makeText(requireContext(), "Đã chọn file nhạc", Toast.LENGTH_SHORT).show();
                }
            });

    private final ActivityResultLauncher<Intent> imagePicker = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    Toast.makeText(requireContext(), "Đã chọn ảnh bìa", Toast.LENGTH_SHORT).show();
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_songs, container, false);

        recyclerSongs = view.findViewById(R.id.recycler_songs);
        recyclerSongs.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new AdminSongAdapter();
        recyclerSongs.setAdapter(adapter);

        adapter.setOnItemClickListener(new AdminSongAdapter.OnItemClickListener() {
            @Override
            public void onEdit(Song song) {
                showUploadDialog(song);
            }

            @Override
            public void onDelete(Song song) {
                deleteSong(song.get_id());
            }
        });

        view.findViewById(R.id.fab_add_song).setOnClickListener(v -> showUploadDialog(null));

        loadAllSongs();

        return view;
    }

    private void showUploadDialog(@Nullable Song songToEdit) {
        // Reset URIs when opening dialog to avoid using stale files
        songUri = null;
        imageUri = null;

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_song_info, null);
        builder.setView(dialogView);

        EditText etTitle = dialogView.findViewById(R.id.et_title);
        EditText etArtist = dialogView.findViewById(R.id.et_artist);
        EditText etGenre = dialogView.findViewById(R.id.et_genre);
        Button btnChooseSong = dialogView.findViewById(R.id.btn_choose_song);
        Button btnChooseImage = dialogView.findViewById(R.id.btn_choose_image);
        Button btnUpload = dialogView.findViewById(R.id.btn_upload);

        if (songToEdit != null) {
            etTitle.setText(songToEdit.getTitle());
            etArtist.setText(songToEdit.getArtist());
            etGenre.setText(songToEdit.getGenre() != null ? songToEdit.getGenre().getId() : "");
        }

        btnChooseSong.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("audio/*");
            songPicker.launch(intent);
        });

        btnChooseImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            imagePicker.launch(intent);
        });

        AlertDialog dialog = builder.create();

        btnUpload.setOnClickListener(v -> {
            String title = etTitle.getText().toString();
            String artist = etArtist.getText().toString();
            String genreId = etGenre.getText().toString();

            if (title.isEmpty() || artist.isEmpty() || genreId.isEmpty()) {
                Toast.makeText(requireContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (songToEdit == null) {
                uploadSong(title, artist, genreId);
            } else {
                updateSong(songToEdit.get_id(), title, artist, genreId);
            }
            dialog.dismiss();
        });

        dialog.show();
    }

    private byte[] readBytesFromUri(Uri uri) throws Exception {
        InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    private void uploadSong(String title, String artist, String genreId) {
        // Validate that songFile is selected (required by backend)
        if (songUri == null) {
            Toast.makeText(requireContext(), "Vui lòng chọn file nhạc", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Sử dụng MIME type chính xác: audio/mpeg cho MP3
            MultipartBody.Part songFile = MultipartBody.Part.createFormData("songFile", "song.mp3",
                    RequestBody.create(readBytesFromUri(songUri), MediaType.parse("audio/mpeg")));

            MultipartBody.Part albumImage = null;
            if (imageUri != null) {
                albumImage = MultipartBody.Part.createFormData("albumImage", "cover.jpg",
                        RequestBody.create(readBytesFromUri(imageUri), MediaType.parse("image/jpeg")));
            }

            RequestBody titlePart = RequestBody.create(title, MediaType.parse("text/plain"));
            RequestBody artistPart = RequestBody.create(artist, MediaType.parse("text/plain"));
            RequestBody genrePart = RequestBody.create(genreId, MediaType.parse("text/plain"));

            ApiService api = RetrofitClient.getClient().create(ApiService.class);
            api.uploadSong(titlePart, artistPart, genrePart, songFile, albumImage)
                    .enqueue(new Callback<Song>() {
                        @Override
                        public void onResponse(Call<Song> call, Response<Song> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(requireContext(), "Upload thành công!", Toast.LENGTH_SHORT).show();
                                loadAllSongs();
                            } else {
                                // Log chi tiết lỗi từ server
                                String errorMsg = "Upload thất bại! Code: " + response.code();
                                try {
                                    if (response.errorBody() != null) {
                                        errorMsg += "\n" + response.errorBody().string();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Song> call, Throwable t) {
                            Toast.makeText(requireContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                            t.printStackTrace();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Lỗi đọc file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void updateSong(String songId, String title, String artist, String genreId) {
        try {
            // Optional: Update song file if selected
            MultipartBody.Part songFile = null;
            if (songUri != null) {
                songFile = MultipartBody.Part.createFormData("songFile", "song.mp3",
                        RequestBody.create(readBytesFromUri(songUri), MediaType.parse("audio/mpeg")));
            }

            // Optional: Update album image if selected
            MultipartBody.Part albumImage = null;
            if (imageUri != null) {
                albumImage = MultipartBody.Part.createFormData("albumImage", "cover.jpg",
                        RequestBody.create(readBytesFromUri(imageUri), MediaType.parse("image/jpeg")));
            }

            RequestBody titlePart = RequestBody.create(title, MediaType.parse("text/plain"));
            RequestBody artistPart = RequestBody.create(artist, MediaType.parse("text/plain"));
            RequestBody genrePart = RequestBody.create(genreId, MediaType.parse("text/plain"));

            ApiService api = RetrofitClient.getClient().create(ApiService.class);
            api.updateSong(songId, titlePart, artistPart, genrePart, songFile, albumImage)
                    .enqueue(new Callback<Song>() {
                        @Override
                        public void onResponse(Call<Song> call, Response<Song> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(requireContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                                loadAllSongs();
                            } else {
                                // Log chi tiết lỗi từ server
                                String errorMsg = "Cập nhật thất bại! Code: " + response.code();
                                try {
                                    if (response.errorBody() != null) {
                                        errorMsg += "\n" + response.errorBody().string();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Song> call, Throwable t) {
                            Toast.makeText(requireContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                            t.printStackTrace();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Lỗi đọc file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteSong(String songId) {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        api.deleteSong(songId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                    loadAllSongs();
                } else {
                    Toast.makeText(requireContext(), "Xóa thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(requireContext(), "Lỗi xóa", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAllSongs() {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        api.getAllSongs(1, 100, "").enqueue(new Callback<SongResponse>() {
            @Override
            public void onResponse(Call<SongResponse> call, Response<SongResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Song> list = response.body().getSongs();
                    adapter.setSongs(list);
                }
            }

            @Override
            public void onFailure(Call<SongResponse> call, Throwable t) {
                Toast.makeText(requireContext(), "Lỗi tải danh sách bài hát", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
