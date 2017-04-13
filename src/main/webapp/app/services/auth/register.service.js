(function () {
    'use strict';

    angular
        .module('wsLivraisonApp')
        .factory('Register', Register);

    Register.$inject = ['$resource'];

    function Register ($resource) {
        return $resource('api/register', {}, {});
    }
})();
