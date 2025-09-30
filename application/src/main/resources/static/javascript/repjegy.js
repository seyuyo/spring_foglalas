$(document).ready(function () {

    jaratokContainer = $("#jaratok-container");
    jaratokTable = $("#jaratok-table");
    jaratokHeader = $("#jaratok-thead");
    showJaratok = $('#show-jaratok-button');
    showJaratok2 = $('#show-jaratok-button2');

    footer = $('footer');
    aside = $('aside');

    footer.css('position', 'fixed');
    aside.hide();

    function footerPosition() {
        if (footer.css('position') === 'fixed') {
            footer.css('position', 'relative');
        } else {
            footer.css('position', 'fixed');
        }
    }

    showJaratok.click(function () {
        sessionStorage.setItem('activeAction', 'jaratok');
        footerPosition();
    });

    showJaratok2.click(function () {
        sessionStorage.setItem('activeAction', 'jaratok');
        footerPosition();

    });

    // Oldalbetöltődéskor végrehajtandó kód
    const activeAction = sessionStorage.getItem('activeAction');

    if (activeAction === 'jaratok') {
        //megjelelnítjük a jaratok containert
        setTimeout(function () {
            jaratokContainer.show();
            jaratokTable.show();
            jaratokHeader.show();
            footerPosition();
            aside.show();
            sessionStorage.removeItem('activeAction');
        }, 100);
    }

});