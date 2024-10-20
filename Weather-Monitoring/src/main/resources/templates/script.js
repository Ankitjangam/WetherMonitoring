document.getElementById('fetchWeather').addEventListener('click', function() {
    const city = document.getElementById('city').value;
    const weatherResult = document.getElementById('weatherResult');

    fetch(`http://localhost:8080/latest/${city}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Weather data not found for city: ' + city);
            }
            return response.json();
        })
        .then(data => {
            weatherResult.innerHTML = `
                <h3>Latest Weather in ${city}</h3>
                <p>Main: ${data.main}</p>
                <p>Temperature: ${data.temp} °C</p>
                <p>Feels Like: ${data.feels_like} °C</p>
                <p>Timestamp: ${new Date(data.dt * 1000).toLocaleString()}</p>
            `;
        })
        .catch(error => {
            weatherResult.innerHTML = `<p style="color: red;">${error.message}</p>`;
        });
});

document.getElementById('fetchDailySummaries').addEventListener('click', function() {
    const city = document.getElementById('city').value;
    const dailySummariesResult = document.getElementById('dailySummariesResult');

    fetch(`http://localhost:8080/daily-summaries/${city}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Daily summaries not found for city: ' + city);
            }
            return response.json();
        })
        .then(data => {
            if (data.length === 0) {
                dailySummariesResult.innerHTML = `<p>No daily summaries found for ${city}.</p>`;
                return;
            }
            dailySummariesResult.innerHTML = `<h3>Daily Summaries for ${city}</h3>`;
            data.forEach(summary => {
                dailySummariesResult.innerHTML += `
                    <p>Date: ${new Date(summary.date).toLocaleDateString()}</p>
                    <p>Main: ${summary.main}</p>
                    <p>Temperature: ${summary.temp} °C</p>
                    <p>Feels Like: ${summary.feels_like} °C</p>
                    <hr>
                `;
            });
        })
        .catch(error => {
            dailySummariesResult.innerHTML = `<p style="color: red;">${error.message}</p>`;
        });
});
