$(document).ready(function () {

    // szálloda gombok
    const szallodaButton = $('#szalloda-button');
    const szallodaContainer = $('.szalloda-container');


    // légitársaság gombok
    const legitarsasagButton = $('#legitarsasag-button');
    const legitarsasagContainer = $('.legitarsasag-container');

    // felhasználó gombok
    const felhasznaloButton = $('#felhasznalo-button');
    const felhasznaloContainer = $('.felhasznalo-container');

    // biztosító gombok
    const biztositoButton = $('#biztosito-button');
    const biztositoContainer = $('.biztosito-container');

    // biztosító csomag gombok
    const biztositoCsomagButton = $('#biztosito-csomag-button');
    const biztositoCsomagContainer = $('.biztosito-csomag-container');

    // ajánlat gombok
    const arajanlatButton = $('#arajanlat-button');
    const arajanlatContainer = $('.arajanlat-container');


    // járat gombok
    const jaratButton = $('#jarat-button');
    const jaratContainer = $('.jarat-container');

    // aktív felhasználó gombok
    const activeUsersButton = $('#active-users-button');
    const activeUsersContainer = $('.active-users-container');


    function hideAllContainers() {
        jaratContainer.hide();
        szallodaContainer.hide();
        legitarsasagContainer.hide();
        felhasznaloContainer.hide();
        biztositoContainer.hide();
        biztositoCsomagContainer.hide();
        arajanlatContainer.hide();
        activeUsersContainer.hide();
    }

    // gombok eseménykezelői
    jaratButton.click(function () {
        hideAllContainers();
        jaratContainer.toggle();
        sessionStorage.setItem('activeAction', 'jarat');
    });

    szallodaButton.click(function () {
        hideAllContainers();
        szallodaContainer.toggle();
        sessionStorage.setItem('activeAction', 'szalloda');
    });

    legitarsasagButton.click(function () {
        hideAllContainers();
        legitarsasagContainer.toggle();
        sessionStorage.setItem('activeAction', 'legitarsasag');
    });

    felhasznaloButton.click(function () {
        hideAllContainers();
        felhasznaloContainer.toggle();
        sessionStorage.setItem('activeAction', 'felhasznalo');
    });

    biztositoButton.click(function () {
        hideAllContainers();
        biztositoContainer.toggle();
        sessionStorage.setItem('activeAction', 'biztosito');
    });

    biztositoCsomagButton.click(function () {
        hideAllContainers();
        biztositoCsomagContainer.toggle();
        sessionStorage.setItem('activeAction', 'biztosito-csomag');
    });

    arajanlatButton.click(function () {
        hideAllContainers();
        arajanlatContainer.toggle();
        sessionStorage.setItem('activeAction', 'arajanlat');
    });

    activeUsersButton.click(function () {
        hideAllContainers();
        activeUsersContainer.toggle();
        sessionStorage.setItem('activeAction', 'active-users');
    });


    
    
// Oldalbetöltődéskor végrehajtandó kód
    const activeAction = sessionStorage.getItem('activeAction');

    if( activeAction === 'jarat' ) {
        jaratButton.trigger('click');
    }else if( activeAction === 'szalloda' ) {
        szallodaButton.trigger('click');
    }else if( activeAction === 'legitarsasag' ) {
        legitarsasagButton.trigger('click');
    }else if( activeAction === 'felhasznalo' ) {
        felhasznaloButton.trigger('click');
    }else if( activeAction === 'biztosito' ) {
        biztositoButton.trigger('click');
    }else if( activeAction === 'biztosito-csomag' ) {
        biztositoCsomagButton.trigger('click');
    }else if( activeAction === 'arajanlat' ) {
        arajanlatButton.trigger('click');
    }else if( activeAction === 'active-users' ) {
        activeUsersButton.trigger('click');
    }

});